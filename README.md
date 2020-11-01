# PROJECT DESCRITION:
This project has been realized for the Intelligent Systems course, being teaching by Dr. Octavio Loyola-Gonzalez, 
At Tecnologico de Monterrey, campus Puebla.

A kaggle competition "Spam Email Detection" has been created for the course.

The purpose was to build a system able to detect spam email, with a content-based approach.

Kaggle website has been used to download Training and Testing files, and to evaluate the system accuracy.
Weka software has been used for the classification. 

The final solution proposed is :
Naïve Bayes Multinomial Text classifier from Weka software, with a class balancer pre-processing.
Training and testing file have been processed with the method SpamEmailDetection.FinalProjet.adaptForWeka(in, out)
It replaces <'> by <\'> in order to be able to load the file in Weka. 

The IDE for the project is : IntelliJ IDEA

The folder images contains pieces of evidence of the participation in the Kaggle competition.

Author: Gaspard GOUPY


# PROJECT DIRECTORIES:
- src/
-> Contains the source code used in the project
	* src/SpamEmailDetection/
	* src/ToKaggle
	
- in/
-> Contains the files to process

- out/
-> Contains the processed files

- latex/
-> Contains the latex source code

- submissions/
-> Contains some of the files submitted to Kaggle

- images/
-> Contains images of the project