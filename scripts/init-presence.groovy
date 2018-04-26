@GrabConfig(systemClassLoader=true)
@Grab(group='org.postgresql', module='postgresql', version='9.4-1205-jdbc42')
import groovy.sql.Sql


def dbUrl      = "jdbc:postgresql://${System.getenv("POSTGRES_HOST")}:${System.getenv("POSTGRES_PORT")}/${System.getenv("POSTGRES_DB")}"
def dbUser     =  System.getenv("POSTGRES_USERNAME")
def dbPassword = System.getenv("POSTGRES_PASSWORD")
def dbDriver   = "org.postgresql.Driver"

def CHILD_STMT = """
  SELECT
    id
  FROM
    child;
""";

def PRESENCE_STMT = """
  INSERT INTO
    presence
  (
    date,
    state,
    child_id,
    absence_reason,
    last_modification,
    author
  ) values (
    :date,
    :state,
    :child_id,
    :absence_reason,
    :last_modification,
    :author
  );
""";

def withSql = { cb ->
  def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
    cb(sql)
    sql?.close()
}

def run = {
  withSql { sql ->
    def childrenIds = sql.rows(CHILD_STMT);
    childrenIds.each { row ->
      try{
          def date = new java.sql.Date(new Date().getTime())
          def dateTime = new java.sql.Timestamp(new Date().getTime())
          def params = [
            date: date,
            state: 'A',
            child_id: row.id,
            absence_reason: '',
            last_modification: dateTime,
            author: 'system'
          ]
          sql.execute(params, PRESENCE_STMT)
      } catch(Exception e) {
        println "Failed to create presence for child $row.id"
      }
    }
  }
}
def retry
retry =  {cb, taskDesc, nbTimes,  waitingTime ->
  try {
    if(nbTimes > 0){
      cb()
      println "Completed task $taskDesc"
    } else {
      println "No more tries for task $taskDesc"
      System.exit(1)
    }
  } catch ( Exception  e) {
    println "Failed to run task: $taskDesc "
    e.printStackTrace()
    println "Will retry in $waitingTime ms"
    Thread.sleep(waitingTime)
    retry(cb, taskDesc, nbTimes - 1, waitingTime)
  }
}
retry(run, "init_presence", 10, 1000 * 10)
