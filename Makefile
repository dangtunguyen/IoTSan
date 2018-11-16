init:
	git fetch --recurse-submodules
	git init
	cd lib/groovy-lang
	./gradlew clean dist
	echo "done"