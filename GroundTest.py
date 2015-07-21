#!/usr/bin/python
# -*- coding: utf-8 -*-
import httplib, urllib
import json

class GroundTest():
	headers = {"Content-type": "application/json", "Accept": "text/plain", "SessionKey": "ll2MnDovbm835tDvvHye6ei7u3STDAGkSEgIgAIh"}
	conn = httplib.HTTPConnection("altair.vps.phps.kr", 9000)
	#headers = {"Content-type": "application/json", "Accept": "text/plain", "SessionKey": "vDmd2HBoULXy3CQwCn8183v1y1FwtTqpIAoiQR9G"}
	#conn = httplib.HTTPConnection("altair.vps.phps.kr", 9001)

	def setUp(self):
		pass

	def tearDown(self):
		self.conn.close()
		pass

	def registerGround(self, name, address, longitude, latitude):
		data = {}
		data['name'] = name
		data['address'] = address
		data['longitude'] = longitude
		data['latitude'] = latitude

		body = json.dumps(data)
		self.conn.request("POST", "/register_ground", body, self.headers)
		response = self.conn.getresponse()
		self.assertEquals(0, response.status)
		return response.read()

	def searchMatch(self, startTime, endTime, latitude, longitude, distance):
		data = {}
		data['startTime'] = startTime
		data['endTime'] = endTime
		data['latitude'] = latitude
		data['longitude'] = longitude
		data['distance'] = distance

		body = json.dumps(data)
		self.conn.request("POST", "/search_match", body, self.headers)
		response = self.conn.getresponse()
		return response.read()

	def editProfile(self, name, address):
		data = {}
		data['name'] = name
		data['address'] = address

		body = json.dumps(data)
		self.conn.request("POST", "/edit_profile", body, self.headers)
		response = self.conn.getresponse()
		return response.read()

	def pushSurvey(self, teamId, matchId):
		data = {}
		data['teamId'] = teamId
		data['matchId'] = matchId

		body = json.dumps(data)
		self.conn.request("POST", "/push_survey", body, self.headers)
		response = self.conn.getresponse()
		return response.read()

	def postList(self, teamId, cur):
		data = {}
		data['teamId'] = teamId
		data['cur'] = cur

		body = json.dumps(data)
		self.conn.request("POST", "/post_list", body, self.headers)
		response = self.conn.getresponse()
		return response.read()

gt = GroundTest()
print gt.postList(126, 0)


