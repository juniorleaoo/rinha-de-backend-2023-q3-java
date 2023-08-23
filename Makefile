build:
	./gradlew build

docker-build:
	docker build -t rinha-backend-java:latest .

docker-tag:
	docker tag rinha-backend-java:latest juniorleaoo/rinha-backend-java:latest

docker-push:
	docker push juniorleaoo/rinha-backend-java:latest

docker-all: build docker-build docker-tag docker-push