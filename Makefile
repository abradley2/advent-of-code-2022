build: build_client uberjar
	java -jar target/uberjar/advent-of-code-2022.jar

clean:
	rm -rf target
	rm -rf resources/public
	rm -rf client/node_modules
	rm -rf client/dist

uberjar: target/uberjar/advent-of-code-2022.jar
	@echo "server built"

target/uberjar/advent-of-code-2022.jar:
	lein uberjar

build_client: resources/public/index.html
	@echo "cleint built"

resources/public/:
	mkdir resources/public

client/node_modules/: resources/public/
	cd client && npm install

resources/public/index.html: client/node_modules/
	cd client && npm run build
	mv client/dist/* resources/public