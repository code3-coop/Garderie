Garderie
================================================================================


Prerequis
-----------
- install docker for mac


Run (and watch to restart the app)
-------------
    docker-compose up webapp

Build (that could trigger a restart of the app in the other terminal)
------------
  docker-compose up build_webapp

Once the app is started you may want to load the seed data into you local database.

Seed data generation
-----------------------

Seed data can be generated and pushed to you local db.

  docker up seed_generator
