
echo "Deploying Curiosity Monolith"
kubectl apply -f appconfig/curiositymonolith-namespace.yaml;
kubectl apply -f ./databaseconfig/.
sleep 15;
kubectl -n curiositymonolith exec -it  `kubectl -n curiositymonolith get \
 --no-headers=true pods -l app=mysql-db -o custom-columns=:metadata.name` \
 -- mysql -h 127.0.0.1 -u root -pmySQLpword#2023 < ./databaseconfig/create-curiositydb-resources.sql;
kubectl apply -f ./appconfig/. ;
echo "updating for a public accessible image of the application"
kubectl -n curiositymonolith set image deployment/curiositymonolith-deployment curiosity=fharris/curiosity:latest
sleep 10;
kubectl get pods -n curiositymonolith;
