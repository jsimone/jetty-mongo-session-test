MongoDB-backed Session State Sample
===================================


Run Locally
-----------

# Build

Build the project with

    mvn package

# Configure

    export MONGOHQ_URL=mongodb://foo:foo@127.0.0.1:27017/test

# Run

Now you can run your webapp with:

    $ sh target/bin/webapp


Run on Heroku
-------------

# Create the app on Heroku

    heroku create --stack cedar --addons mongohq:free

# Deploy to Heroku (assuming the files are already in a git repo)

    git push heroku master

