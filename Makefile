init:
	git fetch --recurse-submodules
	git submodule update --recursive --remote
	cd lib/groovy-lang && ./gradlew clean dist
	echo "done"