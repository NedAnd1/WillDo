# WillDo, Shared Tasks App
Doing more while writing less with Angular and Spring MVC.

## Motivation
The Angular framework is a beast.<br/>
React is notorious as being easier to learn and use by developers,<br/>
while Angular is touted as being hollistic with many capabilites.

The complexity of Angular applications can be staggering,<br/>
and with this project we wanted to tame the beast so-to-speak,<br/>
as we forrayed into the world of Angular for the 1st time.

Angular applications can quickly become very verbose<br/>
with boilerplate code being repeated all over<br/>
for things as common as communicating with the server.

Our goal was to eliminate duplication across the client,<br/>
by harnessing TypeScript's powerful object-oriented capabilities,<br/>
while increasing ease-of-development, flexibility, and maintainability.

We felt this to be especially important,<br/>
since Angular is often geared towards businesses and large scale projects.

## Architecture
For communicating with the back-end,<br/>
I created a generic, extensible [base service](client/src/app/core/api.service.ts),<br/>
and the services responsible for each API endpoint,<br/>
only contain the logic that differentiates it from other endpoints,<br/>
which is often only [the model and url](client/src/app/groups/groups.service.ts).

In many Angular projects the "model" is simply an interface of basic properties,<br/>
because returning a true OOP object with constructors and methods isn't easy,<br/>
but the base service automatically marshals almost any model you throw at it.

This allows our models to encapsulate the logic multiple components would otherwise repeat,<br/>
and eliminates component dependencies on the server's API,<br/>
with the same kinds of property decorators used by Spring,<br/>
being used by the models for resolving any differences between the client and server.

If the client is being built at a different pace or the server is 3rd-party,<br/>
this design might be just what you need.

## Application
Task manager for your group/family, push and remind them from anywhere

This app aims to solve chore management within a group. Groups can be made up of friends, family, or roommates that need to manage recurring or one-off tasks. Our web application aims to provide a service that allows users to post, manage, and assign tasks with deadlines, custom notification management, and ease of use. This problem is meant to solve simple communication issues prevalent in groups with a variety of lifestyle habits.

We used [Ionic](https://ionicframework.com/) on top of Angular for the progressive web app,<br/>
Spring MVC for the server, and Mongo for the database.

Execute [runall](runall) on Unix or [runall.cmd](runall.cmd) on Windows to build and run them all.
Prerequisites: [MongoDB Server](https://www.mongodb.com/download-center/community), [Java Development Kit](http://jdk.java.net/13/), and [Node.js](https://nodejs.org/en/download/)
