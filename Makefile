.PHONY: build run stop

build:
	docker build -t wallet-app .

run:
	docker-compose -f docker-compose-all.yml up -d

stop:
	docker-compose -f docker-compose-all.yml down --remove-orphans
