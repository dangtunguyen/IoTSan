init:
	git fetch --recurse-submodules
	git pull --recurse-submodules
	cd lib/groovy-lang && ./gradlew clean dist
	echo "done"