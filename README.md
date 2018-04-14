Garderie
================================================================================


Prerequis
-----------

- Install postgres 9
- Create a database inside that is called garderie
- Install java 10
- Install gradle


Run (and watch to restart the app)
-------------
    gradle bootRun

Build (that could trigger a restart of the app in the other terminal)
------------
  gradle build

Once the app is started you may want to load the seed data into you local database.

Seed data generation
-----------------------

Seed data can be generated and pushed to you local db.

  pip3 install psycopg2-binary
  python3 generate_seed.py
