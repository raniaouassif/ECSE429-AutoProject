# Commands for exploratory session 1

#/todos GET
"curl --request GET \
  --url http://localhost:4567/todos \
  --header 'Content-Type: application/json'"

#/todos GET  - filter : title
"curl --request GET \
  --url 'http://localhost:4567/todos?title=file%20paperwork'"

#/todos GET - filter:  doneStatus 
"curl --request GET \
  --url 'http://localhost:4567/todos?doneStatus=false'"

#/todos HEAD
"curl --request HEAD \
  --url http://localhost:4567/todos \
  --header 'Content-Type: application/json'"

#/todos POST
"curl --request POST \
  --url http://localhost:4567/todos \
  --header 'Content-Type: application/json' \
  --data '{
	""title"":""Start Exploratory Testing"",
	""doneStatus"": false,
	""description"":""For ecse429 course""
}'"

#/todos POST (with ID in body)
"curl --request POST \
  --url http://localhost:4567/todos \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": ""15"", 
	""title"":""Start Exploratory Testing"",
	""doneStatus"": false,
	""description"":""For ecse429 course""
}'"

#/todos/:id GET
"curl --request GET \
  --url http://localhost:4567/todos/9"

#/todos/:id POST - create
"curl --request POST \
  --url http://localhost:4567/todos/10 \
  --header 'Content-Type: application/json' \
  --data '{
	""title"":""Call XYZ"",
	""doneStatus"": true,
	""description"":""call at 7 am .""
}'"

#/todos/:id POST -  update
"curl --request POST \
  --url http://localhost:4567/todos/1 \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": 200,
	""title"": ""project title "",
	""description"": ""new descriontion""
}'"

#/todos/:id HEAD
"curl --request HEAD \
  --url http://localhost:4567/todos/2"

#/todos/:id PUT
"curl --request PUT \
  --url http://localhost:4567/todos/2 \
  --header 'Content-Type: application/json' \
  --data '	{	
			""title"": ""file paperwork"",
			""doneStatus"": true,
			""description"": ""Filed paperwork in 20 min""
	}'"

#/todos/:id PUT with id
"curl --request PUT \
  --url http://localhost:4567/todos/1 \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": 100,
	""title"": ""new title""
}'"

#/todos/:id DELETE
"curl --request DELETE \
  --url http://localhost:4567/todos/2"

#/todos/:id/tasksof DELETE
"curl --request DELETE \
  --url http://localhost:4567/todos/1/tasksof/1"

#/todos/:id GET
"curl --request GET \
  --url http://localhost:4567/todos/1"

#/todos/:id/categories GET
"curl --request GET \
  --url http://localhost:4567/todos/1/categories"

#/todos/:id/categories HEAD
"curl --request HEAD \
  --url http://localhost:4567/todos/1/categories"

#/todos/:id/categories POST
"curl --request POST \
  --url http://localhost:4567/todos/1/categories \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": ""2"", 
	""title"": ""Home"",
	""description"": """"
}'"

#/todos/:id/categories GET
"curl --request GET \
  --url http://localhost:4567/todos/1/categories"

#/todos/:id/categories GET - nonexisting
"curl --request GET \
  --url http://localhost:4567/todos/1000/categories"

#/todos/:id/categories DELETE
"curl --request DELETE \
  --url http://localhost:4567/todos/1/categories/2"

#/todos/:id GET
"curl --request GET \
  --url http://localhost:4567/todos/1"