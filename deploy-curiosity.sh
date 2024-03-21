

echo "############################"
echo "############################"
echo "############################"
echo "############################"
echo "Deploying Curiosity Monolith"
echo "############################"
echo "############################"
echo "############################"
echo "############################"
kubectl apply -f appconfig/curiositymonolith-namespace.yaml;
kubectl apply -f ./databaseconfig/.
echo ".... waiting for MySQL pod to run to run db configuration...Deploying Curiosity Monolith"
echo "############################"
echo "############################"
sleep 45;
kubectl -n curiositymonolith exec -it  `kubectl -n curiositymonolith get \
 --no-headers=true pods -l app=mysql-db -o custom-columns=:metadata.name` \
 -- mysql -h 127.0.0.1 -u root -pmySQLpword#2023 < ./databaseconfig/create-curiositydb-resources.sql;
kubectl apply -f ./appconfig/. ;
echo "updating for a public accessible image of the application"
kubectl -n curiositymonolith set image deployment/curiositymonolith-deployment curiosity=fharris/curiosity:latest
echo ".... waiting for the application to get deployed..."
sleep 20;
kubectl get pods -n curiositymonolith;
echo "############################"
echo "Done... Have fun!"
echo "############################"

