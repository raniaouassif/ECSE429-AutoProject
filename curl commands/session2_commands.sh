# Commands for exploratory session 2


#/todos OPTIONS
"curl --request OPTIONS \
  --url http://localhost:4567/todos \
  --header 'Content-Type: application/json'"

#/todos PATCH
"curl --request PATCH \
  --url http://localhost:4567/todos/1 \
  --header 'Content-Type: application/json' \
  --data '{
	""description"": ""new description"",
	""doneStatus"": false
}'"

#/todos GET - multiple filters
"curl --request GET \
  --url 'http://localhost:4567/todos?id=2&title=file%20paperwork&doneStatus=false'"

#/todos POST - title only 
"curl --request POST \
  --url http://localhost:4567/todos \
  --header 'Content-Type: application/json' \
  --data '{
	""title"":""My new TODO""
}'"

#/todos POST - without title 
"curl --request POST \
  --url http://localhost:4567/todos \
  --header 'Content-Type: application/json' \
  --data '{
	""description"":"""", 
	""doneStatus"": true
}'"

#/todos/:id  PUT - without title 
"curl --request PUT \
  --url http://localhost:4567/todos/2 \
  --header 'Content-Type: application/json' \
  --data '{
	""description"": ""to do by tomorrow night. ""
}'"

#/todos/:id GET -  nonexisting 
"curl --request GET \
  --url http://localhost:4567/todos/100"

#/todos/:id  PUT - nonexisting
"curl --request PUT \
  --url http://localhost:4567/todos/100 \
  --header 'Content-Type: application/json' \
  --data '{
	""title"": ""new title of nonexistent"",
	""description"": ""nonexistent""
}'"

#/todos/:id  HEAD - nonexisting
"curl --request HEAD \
  --url http://localhost:4567/todos/100"

#/todos/:id  DELETE - nonexisting
"curl --request DELETE \
  --url http://localhost:4567/todos/100"

#/todos/:id/tasksof GET
"curl --request GET \
  --url http://localhost:4567/todos/1/tasksof \
  --header 'Content-Type: application/json'"

#/todos/:id/tasksof GET - nonexisting
"curl --request GET \
  --url http://localhost:4567/todos/100/tasksof \
  --header 'Content-Type: application/json'"

#/todos/:id/tasksof HEAD
"curl --request HEAD \
  --url http://localhost:4567/todos/1/tasksof"

#/todos/:id/tasksof HEAD - nonexisting
"curl --request HEAD \
  --url http://localhost:4567/todos/100/tasksof"

#/todos/:id/tasksof POST
"curl --request POST \
  --url http://localhost:4567/todos/3/tasksof \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": ""1""
}'"

#/todos/:id GET
"curl --request GET \
  --url http://localhost:4567/todos/3"

#/todos/:id/tasksof DELETE
"curl --request DELETE \
  --url http://localhost:4567/todos/3/tasksof/1 \
  --header 'Content-Type: application/json'"

#/todos/:id/tasksof GET
"curl --request GET \
  --url http://localhost:4567/todos/1/tasksof \
  --header 'Content-Type: application/json'"

#/todos/:id/tasksof POST - nonexisting todo 
"curl --request POST \
  --url http://localhost:4567/todos/100/tasksof \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": ""1""
}'"

#/todos/:id/tasksof POST - nonexisting project 
"curl --request POST \
  --url http://localhost:4567/todos/3/tasksof \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": ""100""
}'"

#/todos/:id PUT
"curl --request PUT \
  --url http://localhost:4567/todos/1 \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": 100,
	""title"": ""new title""
}'"

#/todos/:id/tasksof GET - after put
"curl --request GET \
  --url http://localhost:4567/todos/1/tasksof \
  --header 'Content-Type: application/json'"

#/todos/:id/tasksof POST
"curl --request POST \
  --url http://localhost:4567/todos/1/tasksof \
  --header 'Content-Type: application/json' \
  --data '{
	""id"": ""1""
}'"

#/todos/:id/tasksof GET
"curl --request GET \
  --url http://localhost:4567/todos/1/tasksof \
  --header 'Content-Type: application/json'"

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
  --url http://localhost:4567/todos/1/categories \
  --header 'Content-Type: application/json'"

#/todos/:id PUT
"curl --request PUT \
  --url http://localhost:4567/todos/1 \
  --header 'Content-Type: application/json' \
  --data '{
	""title"": ""new title""
}'"

#/todos/:id/categories GET - after put
"curl --request GET \
  --url http://localhost:4567/todos/1/categories"

#/shutdown GET
"curl --request GET \
  --url http://localhost:4567/shutdown"