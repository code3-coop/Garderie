import datetime, calendar, sys, random, os, psycopg2, time


#=======================
#= Generate sample data
#=======================

family_names      = ['Tremblay','Gagnon','Roy','Bouchard','Gauthier','Morin','Lavoie','Fortin','Gagné','Nadeau','Leclerc','Desjardins','Boudreau','Paradis','Boyer']
female_firstnames = ['Stéphanie','Vanessa','Catherine','Nadine','Caroline','Annie']
male_firstnames   = ['Maxime','Alexandre','Jonathan','Mathieu','David','Simon','Sébastien','Vincent','Michael','Samuel']
child_firstnames  = [['Stephen','William','Nathan','Thomas','Jacob','Olivier','Felix','Antoine','Liam','Noah','Xavier'],
                     ['Marine','Lea','Emma','Olivia','Florence','Alice','Zoe','Rosalie','Juliette','Chloé','Eva']]
groups            = [{"name":"Tulipes","educator":"Chantal"},
                     {"name":"Papillons","educator":"Katia"}]
absence_reasons   = ['Malade','Congé','Autre']
area_codes        = [514,450]
number_of_childs  = 7
male              = 0
female            = 1
image_urls        = []
queries           = []
now = datetime.datetime.now()

def loadImageUrls():
    list = os.listdir('src/main/resources/public/img/avatars/male/')
    male_images = []
    for file in list:
        male_images.append("/img/avatars/male/" + file)
    list = os.listdir('src/main/resources/public/img/avatars/female/')
    female_images = []
    for file in list:
        female_images.append("/img/avatars/female/" + file)
    return male_images,female_images

def getRangeDate(now):
    month = now.month
    year = now.year
    (_,num_days) = calendar.monthrange(year, month)
    first_day = datetime.date(year, 1, 1)
    last_day = datetime.date(year, month, num_days)
    return first_day,last_day

def getRandomName(names):
    return names[random.randint(0,len(names)-1)]

def getRandomPhoneNumber(area_codes):
    return str(area_codes[random.randint(0,len(area_codes)-1)]) + "-" + str(random.randint(000,999)) + "-" + str(random.randint(0000,9999))

def getRandomBirthdate():
    start_date = datetime.date.today().replace(day=1, month=1, year=2013).toordinal()
    end_date = datetime.date.today().replace(day=31, month=12, year=2013).toordinal()
    random_day = datetime.date.fromordinal(random.randint(start_date, end_date))
    return random_day

def getRandomReason(state,absence_reasons):
    if state == "A":
        return absence_reasons[random.randint(0,len(absence_reasons)-1)]
    else:
        return ""

def getRandomState():
    tmp = random.randint(1,10)
    if tmp == 1 or tmp == 2:
        return "A"
    else:
        return "P"

def createInsertGroupSQL(group_id,group_name,educator_name):
    return "insert into \"group\" (id, name, educator) values (%d,'%s','%s');" % (group_id,group_name,educator_name)

def createInsertParentSQL(parent_id,parent_1_name,parent_2_name,parent_1_phone,parent_2_phone):
    return "insert into parents (id, parent_1_name, parent_2_name, parent_1_phone, parent_2_phone) values (%d,'%s','%s','%s','%s');" % (parent_id,parent_1_name,parent_2_name,parent_1_phone,parent_2_phone)

def createInsertChildSQL(child_id, kid_firstname, kid_name, kid_birthdate, image_url, parent_id, group_id):
    return "insert into child (id, firstname, lastname, birthdate, image_url, parents, group_id) values (%d,'%s','%s','%s','%s',%d,%d);" % (child_id, kid_firstname, kid_name, kid_birthdate, image_url, parent_id, group_id)

def createInsertAttendanceSQL(current_date,state,child_id,reason,last_modification,author):
    return "insert into presence (date, state, child_id, absence_reason, last_modification, author) values ('%s','%s',%d,'%s','%s','%s');" % (current_date,state,child_id,reason,last_modification,author)

def resetDatabase():
    conn = psycopg2.connect(dbname=os.environ["POSTGRES_DB"], user=os.environ.get("POSTGRES_USERNAME"), password=os.environ.get("POSTGRES_PASSWORD"), host=os.environ.get("POSTGRES_HOST"), port=os.environ.get("POSTGRES_PORT"))
    cur = conn.cursor()
    cur.execute("truncate presence CASCADE")
    cur.execute("truncate child CASCADE")
    cur.execute("truncate parents CASCADE")
    cur.execute("truncate \"group\" CASCADE")
    conn.commit()
    cur.close()
    conn.close()

def loadDatabase(queries):
    conn = psycopg2.connect(dbname=os.environ["POSTGRES_DB"], user=os.environ.get("POSTGRES_USERNAME"), password=os.environ.get("POSTGRES_PASSWORD"), host=os.environ.get("POSTGRES_HOST"), port=os.environ.get("POSTGRES_PORT"))
    cur = conn.cursor()
    for query in queries:
        cur.execute(query)
    conn.commit()
    cur.close()
    conn.close()

if __name__ == '__main__':
    image_urls = loadImageUrls()
    (first_day,last_day) = getRangeDate(now)
    group_id = 1
    parent_id = 1
    child_id = 1

    for group in groups:
        queries.append(createInsertGroupSQL(group_id,group["name"],group["educator"]))
        for i in range(1,number_of_childs+1):

            #= build parents
            saved_name = getRandomName(family_names)
            parent_1_name = getRandomName(male_firstnames) + " " + saved_name
            parent_2_name = getRandomName(female_firstnames) + " " + getRandomName(family_names)
            parent_1_phone = getRandomPhoneNumber(area_codes)
            parent_2_phone = getRandomPhoneNumber(area_codes)
            queries.append(createInsertParentSQL(parent_id,parent_1_name,parent_2_name,parent_1_phone,parent_2_phone))

            #= build kid
            child_name = saved_name
            child_type = random.randint(0,len(child_firstnames)-1)
            child_image_url = getRandomName(image_urls[child_type])
            child_firstname = getRandomName(child_firstnames[child_type])
            child_birthdate = getRandomBirthdate()
            queries.append(createInsertChildSQL(child_id, child_firstname, child_name, child_birthdate, child_image_url, parent_id, group_id))

            #= Attendance
            delta = last_day - first_day
            for j in range(delta.days + 1):
                current_date = first_day + datetime.timedelta(days=j)
                if current_date.weekday() > 4:
                    state = "W"
                elif current_date == datetime.date(2018, 5, 21):
                    state = "F"
                else:
                    state = getRandomState()
                reason = getRandomReason(state,absence_reasons)
                queries.append(createInsertAttendanceSQL(current_date,state,child_id,reason,now,group["educator"]))
            parent_id += 1
            child_id +=1

        group_id +=1

def run():
    resetDatabase()
    loadDatabase(queries)

def retry(cb, taskName, nbRetry, waitingTime):
    try:
        if nbRetry > 0:
            cb()
            print(f"Completed task {taskName}")
        else:
            print(f"Failed to run task {taskName}")
            return 1
    except Exception as e:
        print(f"Failed to run {taskName}")
        print(e)
        print(f"Will retry {taskName} in {waitingTime}ms")
        time.slept(waitingTime)
        retry(cb, taskName, nbRetry - 1, waitingTime)

retry(run, "generate_seed", 5, 10000)
