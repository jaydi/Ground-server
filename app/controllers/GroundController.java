package controllers;

import java.util.ArrayList;
import java.util.List;

import models.GroundEntity;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.modules.router.Post;
import protocols.DefaultResponse.ResponseCode;
import protocols.ErrorResponse;
import protocols.Ground;
import protocols.RegisterGroundRequest;
import protocols.RegisterGroundResponse;
import protocols.SearchGroundRequest;
import protocols.SearchGroundResponse;
import utils.ProtocolCodec;

public class GroundController extends ApplicationController {
	@Post("/register_ground")
	public static void registerGround(String body) throws Exception {
		RegisterGroundRequest request = ProtocolCodec.decode(body, RegisterGroundRequest.class);
		Logger.info(request.toString());

		String name = request.getName();
		String address = request.getAddress();
		double latitude = request.getLatitude();
		double longitude = request.getLongitude();

		if (StringUtils.isEmpty(name))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		GroundEntity ground = new GroundEntity();

		ground.setAddress(address);
		ground.setName(name);
		ground.setLatitude(latitude);
		ground.setLongitude(longitude);
		ground.save();

		RegisterGroundResponse response = new RegisterGroundResponse();
		response.setGroundId(ground.getId());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/search_ground")
	public static void serchGround(String body) throws Exception {
		SearchGroundRequest request = ProtocolCodec.decode(body, SearchGroundRequest.class);
		Logger.info(request.toString());

		String name = request.getName();
		StringBuilder sb = new StringBuilder("%");
		sb.append(name);
		sb.append("%");
		List<GroundEntity> groundEntityList = GroundEntity.find("name like ? order by freq desc", sb.toString()).fetch(10);

		List<Ground> groundList = new ArrayList<Ground>();
		for (GroundEntity groundEntity : groundEntityList)
			groundList.add(groundEntity.toGround());

		SearchGroundResponse response = new SearchGroundResponse();
		response.setGroundList(groundList);

		renderJSON(ProtocolCodec.encode(response));
	}
}
