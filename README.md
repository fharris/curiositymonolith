Curiosity Monolith
---

This is the code for the backend in the monolith version, part of Chapter 7 of the book [Cloud Native Architecture: Efficiently moving legacy applications and monoliths to microservices and Kubernetes]([./automatic-deployment.md](https://www.amazon.co.uk/dp/B0D5LK21XB?crid=2HO2SA1E3S24&dib=eyJ2IjoiMSJ9.2zYJXLCG7WYDkyyWvJDrt7_Vd0BK0DkJTCeZ9zJfnVEs7lsFCOUZ0goOGClx0O1Xe4i0RcUsgZTpb64X8wMYWw.ZmCeUfWWEfc9gMOTUbkj6xYb1UcEGq911js3DoQf4VE&dib_tag=se&keywords=fernando+harris&qid=1717143489&sprefix=fernando+harris,aps,59&sr=8-2&linkCode=sl1&tag=homeofthemp3m-21&linkId=4204cd4bd3cf259f004812227bdced95&language=en_GB&ref_=as_li_ss_tl)) .
The lab was initially conceived to deploy the application on a local Kubernetes cluster. The examples were successfully tested with K3s and Rancher Desktop. I will add more examples and exercises and test it with other Kubernetes local runtimes like minikube. Meanwhile, if you find anything you would like to change, improve or correct, let us know!

*Fernando Harris*

---

**Manual deployment**

Install the application manually

[Manual deployment](./manual-deployment.md)

**Automated deployment**

Setup a local full DevOps ecosystem with Jenkins CI/CD pipelines, Gogs as a source code server repository and Registry to manage container images life cycle.

[Automated deployment](./automatic-deployment.md)

