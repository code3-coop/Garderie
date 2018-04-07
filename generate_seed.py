import datetime, calendar, sys, random
#=======================
#= Generate sample data
#=======================

family_names      = ['Tremblay','Gagnon','Roy','Bouchard','Gauthier','Morin','Lavoie','Fortin','Gagné']
female_firstnames = ['Stéphanie','Vanessa','Catherine']
male_firstnames   = ['Maxime','Alexandre','Jonathan','Mathieu','David','Simon','Sébastien','Vincent','Michael','Samuel']
kid_firstnames    = [['William','Nathan','Thomas','Jacob','Olivier','Felix','Antoine','Liam','Noah','Xavier'],
                     ['Lea','Emma','Olivia','Florence','Alice','Zoe','Rosalie','Juliette','Chloé','Eva']]
educator_names    = ["Sylvie"]
group_names       = ["Tulipes"]
absence_reasons   = ['Malade','Congé','Autre']
area_codes        = [514,450]
number_of_groups  = 1
number_of_kids    = 7

now = datetime.datetime.now()

def getRangeDate(now):
    month = now.month
    year = now.year
    (_,num_days) = calendar.monthrange(year, month)
    first_day = datetime.date(year, month, 1)
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

def createInsertChildSQL(kid_id, kid_firstname, kid_name, kid_birthdate, image_url, parent_id, group_id):
    return "insert into child (id, firstname, lastname, birthdate, image_url, parents, \"group\") values (%d,'%s','%s','%s','%s',%d,%d);" % (kid_id, kid_firstname, kid_name, kid_birthdate, image_url, parent_id, group_id)

def createInsertAttendanceSQL(current_date,state,kid_id,reason,last_modification,author):
    return "insert into presence (date, state, child, absence_reason, last_modification, author) values ('%s','%s',%d,'%s','%s','%s');" % (current_date,state,kid_id,reason,current_date,author)


(first_day,last_day) = getRangeDate(now)

group_id = 1
parent_id = 1
kid_id = 1
for group_name in group_names:
    educator_name = getRandomName(educator_names)
    print(createInsertGroupSQL(group_id,group_name,educator_name))
    for kids_id in range(1,number_of_kids+1):

        #build parents
        saved_name = getRandomName(family_names)
        parent_1_name = getRandomName(male_firstnames) + " " + saved_name
        parent_2_name = getRandomName(female_firstnames) + " " + getRandomName(family_names)
        parent_1_phone = getRandomPhoneNumber(area_codes)
        parent_2_phone = getRandomPhoneNumber(area_codes)
        print(createInsertParentSQL(parent_id,parent_1_name,parent_2_name,parent_1_phone,parent_2_phone))

        #build kid
        kid_name = saved_name
        kid_firstname = getRandomName(kid_firstnames[random.randint(0,len(kid_firstnames)-1)])
        kid_birthdate = getRandomBirthdate()
        print(createInsertChildSQL(kid_id, kid_firstname, kid_name, kid_birthdate, 'null', parent_id, group_id))

        #Attendance
        delta = last_day - first_day
        for i in range(delta.days + 1):
            current_date = first_day + datetime.timedelta(days=i)
            state = getRandomState()
            reason = getRandomReason(state,absence_reasons)
            author = educator_name
            print(createInsertAttendanceSQL(current_date,state,kid_id,reason,current_date,author))

        parent_id += 1
        kid_id +=1
