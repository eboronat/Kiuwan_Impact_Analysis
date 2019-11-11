# Kiuwan_Impact_Analysis
POST request example to the Kiuwan's Architecture product to get the impacted components from Java.

## Running the program
Import the project as a Maven one to get dependencies.

1. Set your username and password in the corresponding variables.

2. The program receives 4 arguments: application name, source component name, source component type and navigation direction. You can edit (add, delete, modify) these arguments as you wish. They are used to create a JSON Object that is included into the POST request. You can see the structure of the JSON Object into (https://www.kiuwan.com/docs/display/K5/Architecture#Architecture-%C2%AB%C2%BBListimpactedcomponents).

## Output example
java -jar ImpactAnalysis.jar SAP1 E070 Table in

Executing request POST https://www.kiuwan.com/saas/rest/v1/arch/impact/searchTargets HTTP/1.1
----------------------------------------
HTTP/1.1 200 OK
----------------------------------------
Component Impacted Name: ZCL_IM_KW_BADI_REQ_CHECK (Type: class)
Component Impacted Name: ZKW_CL_CODE (Type: class)
