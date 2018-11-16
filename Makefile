init:
	git submodule update --init --recursive
	cd lib/groovy-lang && ./gradlew clean dist
	echo "done"