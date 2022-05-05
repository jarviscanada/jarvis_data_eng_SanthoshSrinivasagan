#! /bin/sh

cmd=$1
db_username=$2
db_password=$3

# start docker if not running
sudo systemctl status docker || systemctl start docker
container_status=$(docker container ls -a -f name=jrvs-psql | wc -l)

#User switch case to handle create|stop|start opetions
case $cmd in

  create)
  # Check if the container is already created
  if [ $container_status -eq 2 ]; then
		echo "Container already exists"
		exit 1
	fi
  #check # of CLI arguments
  if [ $# -ne 3 ]; then
    echo "Create requires username and password, please follow this format"
    echo "[Bash_File] create [user_name] [password]"
    exit 1
  fi
  #Create container
	docker volume create pgdata
	docker run --name jrvs-psql -e POSTGRES_PASSWORD=${db_password} -e POSTGRES_USER=${db_username} -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
	exit $?
	;;

  start|stop)
  #check instance status; exit 1 if container has not been created
  if [ $container_status -lt 2 ]; then
    echo "Container does not exist"
    exit 1
  fi
  #Start or stop the container
	docker container $cmd jrvs-psql
	exit $?
	;;

  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac

exit 0