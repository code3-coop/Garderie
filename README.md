Garderie
================================================================================


Prerequis
-----------
- install docker for mac


Run (and watch to restart the app)
-------------
  sh start.sh

Build (that could trigger a restart of the app in the other terminal)
------------
  sh build.sh

Once the app is started you may want to load the seed data into you local database.

Seed data generation
-----------------------

Seed data can be generated and pushed to you local db.

  docker-compose up seed_generator

Presence initialisation
------------------------

This system requires that presence are created auto-magically
Do do so, on each day we need to run the following command

  docker-compose up init_presence
