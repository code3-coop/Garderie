//XXX should be runned with groovy 2.16 and java 1.8
@GrabConfig(systemClassLoader=true)
@Grab(group='org.postgresql', module='postgresql', version='9.4-1205-jdbc42')
import groovy.sql.Sql

def dbUrl      = "jdbc:postgresql://localhost/garderie"
def dbUser     = "postgres"
def dbPassword = ""
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

def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
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
sql.close()
