MongoDB-backed Session State Sample
===================================


Run Locally
-----------

### Build the project with Maven:

    mvn package


### Configure the `MONGOHQ_URL` environment variable to point to a local MongoDB instance:

* Linux/Mac

        export MONGOHQ_URL=mongodb://127.0.0.1:27017/test

* Windows

        set MONGOHQ_URL=mongodb://127.0.0.1:27017/test


### Run

Now you can run your webapp with:

* Linux/Mac

        $ sh target/bin/webapp

* Windows

        $ target\bin\webapp.bat

### Try the application in your browser by navigating to:

    http://localhost:8080/


Run on Heroku
-------------

### Install the [Heroku Toolbelt](http://toolbelt.heroku.com)

### Login to Heroku from the command line

    heroku login

In the root directory of this project do the following:

### Create the app on Heroku with the "cedar" stack and the free MongoHQ add-on

    heroku create --stack cedar --addons mongohq:free

### Deploy to Heroku

    git push heroku master

### Open the application in your browser

    heroku open
