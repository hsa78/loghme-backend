Guide to dockerizing commands:

for removing current mydockerapp container use these commands:
sudo docker stop mydockerapp
sudo docker rm mydockerapp

for building myapp image run below command where server Dockerfile is:
sudo docker build -t myapp .

for running image with mydockerapp name run below command(put -d if you want to run it background):
sudo docker run  -p 8080:8080  --name mydockerapp myapp

you can see the running containers with:
sudo docker ps -a

also you can see docker images with:
sudo docker images

if you want to go into a image and see its content:
sudo docker run -it myapp sh

for running sql images the commands are similar so i just put it together:
sudo docker stop my-mysql
sudo docker rm my-mysql
sudo docker build -t my-mysql .
docker run -d -p 3306:3306 --name my-mysql -e MYSQL_ROOT_PASSWORD=supersecret my-mysql

if you get the error of port 3306 is in use(first make sure there is no running container on that port):
sudo service mysql stop

if you get some unordinary error(Can't connect to local MySQL server through socket '/var/run/mysqld/mysqld.sock' (2))(you can search it of course):
mysql -h 127.0.0.1 -P 3306

for confirming sql container:
sudo docker exec -it my-mysql bash
	and then bash commands:
	mysql -uroot -p
	show databases;
	use loghme;
	show tables;

for deploying server on kubernetes:
    if you want to delete previous deployments and PVCs:
        kubectl delete deployment backend
        kubectl delete deployment mysql-deployment
        kubectl delete secret mysql-secrets  //this is for mysql password you can omit it
        kubectl delete persistentvolumeclaim mysql-data-disk
    for deploying database:
        kubectl apply -f database.yaml
        kubectl -n hosna-fatemeh-ns exec -it $mysql-pod-name -- /bin/bash
        in pod bash run this command:
                mysql -p </docker-entrypoint-initdb.d/loghme.sql //password = supersecret
        CTRL+d
    for deploying backend: 
        kubectl apply -f backend.yaml
        
for debugging:
    kubectl describe pod $pod-name
    kubectl logs $pod-name $container-name

for getting resourcequota:
    kubectl get resourcequota mem-cpu-demo --namespace=hosna-fatemeh-ns --output=yaml

http address: http://ie.etuts.ir:30756