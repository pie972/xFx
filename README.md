TO BE CONTINUED
## The project is not yet uploaded completely. Still in progress..
# xFx
xFx is a project in which I can take control of a shared folder under a remote system. More specifically, I can do the following: browse the remote shared folder, including its sub-folders, rename a remote shared file or sub-folder, download a remote shared file, upload a local file to a specified path under the remote shared folder, and delete a shared remote file. Java is used on the remote system side while Python is used on the local system.

In this project, I used the following: XML/SOAP as a technology because we are exposing components of application logic as services rather than data, WSDL (it is an XML notation for describing a web service) as a service definition language, SOAP over HTTP as a protocol because we are exposing a public API over the Internet, Java (JAX-WS API) as a provider programming language, and finally Python (Zeep module) as the consumer programming language in which the client is the main interface for interaction with a SOAP server.

