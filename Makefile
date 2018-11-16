init:
	git fetch --recurse-submodules
	cd lib/groovy-lang && ./gradlew clean dist
	echo "done"