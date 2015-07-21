package controllers;

import java.io.File;
import java.io.FileInputStream;

import models.ImageEntity;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.db.jpa.Blob;
import play.libs.Images;
import play.libs.MimeTypes;
import play.modules.router.Post;
import protocols.DefaultResponse.ResponseCode;
import protocols.DownloadRequest;
import protocols.ErrorResponse;
import protocols.UploadResponse;
import utils.ProtocolCodec;

public class ImageController extends ApplicationController {
	@Post("/upload")
	public static void upload() throws Exception {
		File file = params.get("file", File.class);
		boolean thumbnail = false;

		if (params.get("thumbnail") != null)
			thumbnail = Boolean.parseBoolean(params.get("thumbnail"));

		ImageEntity image = new ImageEntity();

		Blob blob = new Blob();
		blob.set(new FileInputStream(file), MimeTypes.getContentType(file.getName()));

		image.setPath(blob.getUUID());
		image.setData(blob);
		image.setFileName(file.getName());

		if (thumbnail) {
			File thFile = new File(file.getPath() + "_th");
			Images.resize(file, thFile, 100, 100);

			Blob thBlob = new Blob();
			thBlob.set(new FileInputStream(thFile), MimeTypes.getContentType(file.getName()));
			image.setThData(thBlob);
		}

		image.save();

		UploadResponse response = new UploadResponse();
		response.setPath(image.getPath());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/download")
	public static void download(String body) throws Exception {
		DownloadRequest request = ProtocolCodec.decode(body, DownloadRequest.class);
		Logger.info(request.toString());

		String path = request.getPath();
		boolean thumbnail = request.isThumbnail();

		if (StringUtils.isEmpty(path))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "empty path"));

		ImageEntity image = ImageEntity.findById(path);
		if (image == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no such images"));

		if (thumbnail && image.getThData() != null && image.getThData().exists()) {
			response.setContentTypeIfNotSet(image.getThData().type());
			renderBinary(image.getThData().get(), image.getFileName() + "_th");
		} else {
			response.setContentTypeIfNotSet(image.getData().type());
			renderBinary(image.getData().get(), image.getFileName());
		}
	}
}
