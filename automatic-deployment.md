**Requirements**

**16 Gb ram**

**Access to a Kubernetes cluster (k3s, minikube…)**

 •Access to a Kubernetes cluster
•	kubectl installed and pointing to the cluster
•	git
•	jq installed 


**Get the code from GitHub**

If you haven't done so, get the code from the repository:

git clone [https://github.com/fharris/curiositymonolith](https://github.com/fharris/curiositymonolith)

Change to the curiositymonolith folder and run the following commands:

If we already tried this exercise before, or have installed the application manually, please run the following 2 scripts for housekeeping:

./housekeeping-k8s.sh

./housekeeping-docker.sh

1. **Take note of the kube proxy API endpoint.**

In a tab run the command:

kubectl proxy &

Open another tab and run the following command:

curl localhost:8001/api

And you should get the Server address. 

<img width="339" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/de567643-96be-4683-901f-1fda0abf15af">

You will need to update this value later in Jenkins env vars.

1. **Create a namespace for the application and Jenkins user credentials**

./jenkinsconfiguration-k8.sh

1. **Create local network and containers**

We will create a docker network (we could do this with docker composer as well, but for now let's keep it like that) and provision 5 containers that will help us recreate a simplified cloud native ecosystem. A first container with Docker Dind which allows us to build and run containers from within containers. A second container with Jenkins where the CI/CD pipelines to build and deploy the application will be configured. A third container with Gogs, a simple git Server where our code will reside and be synchronized with the Jenkins pipelines. A fourth container with a local mysql database, which we will use to help us on the builds. A fifth container with a local docker registry where the lifecycle of our application image will be managed and Kubernetes pull it to launch. Remember that this exercise is pedagogical and has an educational goal. It's important if you want to learn how things work behind the scenes.

Run the script:

./CICD/containers-run-config.sh

Wait a couple of minutes and at the end of the processconfirm the environment is running (you need jq installed):

docker network inspect cloudnative | jq '.[].Containers'

you should see the docker network cloudnative with 5 containers running, each showing their respective hostnames and local IP addresses.

![](RackMultipart20231003-1-aq9tt0_html_d65480e48be4764a.png)

1. **Configure local Git server**

The Gogs container is running on localhost:10880. Copy past that hostname:port on your browser and start the configuration. The first time you run it, you will get a special screen for the database set-up. Make sure that you select **SQLite3** and keep the **Path** as it is as illustrated in Figure gogs1.

<img width="1036" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/fa65bd6b-305e-450d-9d92-4a3627c9146e">


![](RackMultipart20231003-1-aq9tt0_html_d635a2ca952c171e.png)

Figure gogs1

Below, in the Application General Settings please change the default Branch from **master** to **main** : as illustrated in Figure gogs2:

<img width="942" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/9859ee68-2dfe-4e5d-ba2a-2287ea2c0c76">


![](RackMultipart20231003-1-aq9tt0_html_1f7359bb53b17dcf.png)

Figure gogs2

Now on the optional settings, you will need to define an admin user called gogs-user. Take note of the password you are going to use. The email is optional. Click the blue button Install Gogs. Follow figure gogs3 for more details:

<img width="959" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/33f8fcb5-91b6-4a28-ad09-fadd0e4d6b15">


![](RackMultipart20231003-1-aq9tt0_html_e1991ea04bc71612.png)

**Figure gogs3**

After clicking the button, your browser will probably revert to localhost:3000 and the connection will be lost. Just retype localhost:10880. Before signing in and importing the repository of our Wikipedia application from GitHub, we first need to configure Gogs to allow local calls. In a terminal run the following command to get into the gogs container and add a line to its configuration file app.ini:

docker exec -it gogs sh -c "echo 'LOCAL\_NETWORK\_ALLOWLIST = \*' \>\> /data/gogs/conf/app.ini"

Restart the gogs container with

docker restart gogs

and sign in with the gogs-user and the password you created before. Click the little plus "+" signal next to your avatar and select New Migration as per Figure gogs4:

<img width="985" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/303652e6-1048-4a79-ac4d-8b9600d64f72">


![](RackMultipart20231003-1-aq9tt0_html_647ae7bc2e06a780.png)

Figure gogs4

Follow Figure gogs5 and replace the Clone Address with the GitHub address of the original repository which is [https://github.com/fharris/curiositymonolith](https://github.com/fharris/curiositymonolith) . The owner will be **gogs-user** and the name should be the **curiositymonolith** as well.

<img width="821" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/79d5a50f-48b6-434f-ad4b-3231d17ecdac">


![](RackMultipart20231003-1-aq9tt0_html_6d8b01dc2bcefa6c.png)

Figure gogs5

After clicking the green button to start the migration, if all goes well, you should be able to see your codebase at [http://localhost:10880/gogs-user/curiositymonolith](http://localhost:10880/gogs-user/curiositymonolith) .

We will now configure Webhooks for the Gogs-Jenkins communication. From your codebase click Settings (the little tools icon on the top right of the screen) or navigate directly to [http://localhost:10880/gogs-user/curiositymonolith/settings](http://localhost:10880/gogs-user/curiositymonolith/settings) . Click Webhooks, and Add a New Webooks of type Gogs as per figure gogs6:

<img width="1057" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/280c7bbc-51d1-4a5c-8d0a-1d6b5f178ab6">


![](RackMultipart20231003-1-aq9tt0_html_e1813c6e1ef799f5.png)

Figure gogs6

Replace the Payload URL with [http://jenkins:8080/gogs-webhook/?job=buildcuriosity](http://jenkins:8080/gogs-webhook/?job=buildcuriosity) which is where Jenkins CI/CD will be waiting for Gogs notifications. Make sure the Content-Type is application/json, and that there is no Secret configured. Before clicking the green button to create the Webhook ensure the Just the Push event option is selected and the Active box is enabled. Figure gogs7 shows how to do it:

<img width="997" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/07981dab-68ac-4420-971b-b5ecfe4eb257">


![](RackMultipart20231003-1-aq9tt0_html_b3be40f1268aec96.png)

Figure gogs7

If all goes well, you should get something as Figure gogs8. Click the link for the webhook (should be [http://jenkins:8080/gogs-webhook/?job=buildcuriosity](http://jenkins:8080/gogs-webhook/?job=buildcuriosity) ).



![](RackMultipart20231003-1-aq9tt0_html_13b8dd7759df0b57.png)

Figure gogs8

Now, as illustrated in Figure gogs9, inside the webhook configuration you will see a little Test Delivery button on the right bottom of the screen. If you click it you should get a successful test and an event as just been delivered to your local Jenkins:

<img width="1090" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/fb89e807-40c2-47da-b1a9-f53d3839553a">


![](RackMultipart20231003-1-aq9tt0_html_6f9154031c81211.png)

Figure gogs9

1. **Configuring Jenkins**

This Jenkins container has all the CI/CD pipelines already configured for you to use. All is managed as code from the code base repository itself (the buildcuriosity.groovy and deploycuriosity.groovy). Navigate with your browser to localhost:8080 and sign in with the user we prepared for you which is **admin** with password **123**. Skip all the steps related to plugin installation or related to the creation of new users.

<img width="1281" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/9aea823c-2a7a-422a-ad10-70c7df68aab8">

![](RackMultipart20231003-1-aq9tt0_html_647a325c2ba2b80e.png)

Close this and ignore

![](RackMultipart20231003-1-aq9tt0_html_7ccf605345a563ea.png)

Start using Jenkins

Once logged in you should see Jenkins with 3 jobs configured. The job curiosity should have at least a failed build which was triggered when you tested the Gogs Webhook. The first run takes a few minutes.

<img width="1428" alt="image" src="https://github.com/fharris/curiositymonolith/assets/17484224/2e32f272-bc5e-430e-8dbc-35c033ebf370">


![](RackMultipart20231003-1-aq9tt0_html_f3af156b977ab465.png)

Figure jenk1

We must update the Kubernetes token for the Jenkins Service Account and the local MySQL password to the **curiosity** user, which is **Welcome#1**.

![](RackMultipart20231003-1-aq9tt0_html_4200aab3ef8edfd6.png)

Click Credentials:

![](RackMultipart20231003-1-aq9tt0_html_9cb0e459bae2ea58.png)

**Update Kubernetes token:**

kubectl get secrets jenkins-task-sa-secret -o json | jq -Mr '.data["token"]' | base64 -D

![](RackMultipart20231003-1-aq9tt0_html_4b65a4753b9b0fad.png)

![](RackMultipart20231003-1-aq9tt0_html_e47bcee192f08e62.png)

and replace here the token with copy-past.

**Update Local MySQL password:**

User: curiosity

Password: Welcome#1

Run the build first…we need an image in the local container repos:

![](RackMultipart20231003-1-aq9tt0_html_be3cc375e5563d84.png)

Run the configurecuriosity job to install everything:

![](RackMultipart20231003-1-aq9tt0_html_9099957d1166cc4d.png)

![](RackMultipart20231003-1-aq9tt0_html_623f9f7e813e664e.png)

kubectl -n curiositymonolith port-forward svc/curiositymonolith-service-lb 9000:80

![](RackMultipart20231003-1-aq9tt0_html_e5bf9d8562803b43.png)

![](RackMultipart20231003-1-aq9tt0_html_492032c8afcd81a1.png)
