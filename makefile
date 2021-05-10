deploy:
	echo "Be sure to commit and merge to master"
	git push heroku master

local:
	mvn clean install
	heroku local web