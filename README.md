curiosity.git

This is the code for the backend in the monolith version

Fernando Harris
---

**Manual deployment 

1.	Get the code from GitHub
If you haven’t done so, get the code from the repository with the below command:

git clone https://github.com/fharris/curiositymonolith

2.	Create the curiositymonolith namespace

You can start by running the housekeeping-k8s script first to clean everything if this is not the first time you are doing the set-up.  Change to the curiositymonolith folder and run the following commands:

 ./housekeeping-k8s.sh

If you just want to see the app running and leave the manual steps to study later just run the following script:

./deploy-wikiapp.sh

If all goes well, you may jump to step 5 and ignore the rest of the steps.

If you want to install things step by step, then, once everything is cleaned with the housekeeping script, try to create the namespace with the following command:

kubectl apply -f appconfig/curiositymonolith-namespace.yaml

3.	Deploy database

Deploy the MySQL database with the following command:

kubectl apply -f ./databaseconfig/.

You will notice that the password in the mysql-db-secret.yaml is set to the base64 of mySQLpword#2023. If you change the password, please don’t forget to take note of that.

Configure the mysql database for the application  

When the mysql pod is running, run the following command to create the user curiosity and the database curiositydb:

kubectl -n curiositymonolith exec -it  `kubectl -n curiositymonolith get \
 --no-headers=true pods -l app=mysql-db -o custom-columns=:metadata.name` \
 -- mysql -h 127.0.0.1 -u root -pmySQLpword#2023 < ./databaseconfig/create-curiositydb-resources.sql

4.	Deploy the application

Deploy the application with the following command:

kubectl apply -f ./appconfig/.

5.	Run the application

If all goes well, check that you have successfully deployed the application and the database with the following command:

kubectl get pods -n curiositymonolith

You should be able to see the curiosity monolith and respective database pods running. Let's access the application with:

kubectl -n curiositymonolith port-forward svc/curiositymonolith-service-lb 9000:80

And if you open your browser at http://localhost:9000 , voilà:
![image](https://github.com/fharris/curiositymonolith/assets/17484224/c9bd0364-4d4b-44c9-be4a-e890a15b67f8)

