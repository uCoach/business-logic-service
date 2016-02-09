#Business Logic Service

The business logic service is the main service of System Logic Layer and implements all the internal algorithms and procedures. It interact with authentication-api and data-service and provide a REST-like interface to the Application Layer and to the process-centric-service.

For more information and list of resources/endpoints provided see the [Wiki](../../wiki)

###DEPENDENCIES

	# Authorization keys
	export BUSINESS_AUTH_KEY="business_logic_auth_key"
	export DATA_AUTH_KEY="data_auth_key"

###HOW TO RUN

	git clone https://github.com/uCoach/business-logic-service.git
	cd business-logic-service
	ant run

#####Deployed on Heorku:

  	https://ucoach-business-logic-service.herokuapp.com
