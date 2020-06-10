package edu.bjtu.javaee.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;


@RestController
@RequestMapping("/media")
public class MediaController {
    final long ChunkSize = 1048576L;
    
//	@Autowired
//	CourseDao courseDao;
//	
//	@Autowired
//	MaterialDao materialDao;
//	
//	@Autowired
//	TeacherDao teacherDao;

	@Value("${media.location}")
	private String contentPath;
	
	@RequestMapping(method = RequestMethod.GET, path = "/documents/{name}")
	public ResponseEntity<UrlResource> getFile(@PathVariable("name") String name, @RequestHeader HttpHeaders headers) throws Exception {
		
		//List<Material> materials = materialDao.selectByCourseId(courseid);
		String path = new File("").getAbsolutePath();
		String vpath = "file:///" + path +"\\"+ contentPath+"\\"+name;
		UrlResource doc = new UrlResource(vpath);
        return ResponseEntity.ok()
        		.contentType(MediaTypeFactory.getMediaType(doc).orElse(MediaType.APPLICATION_OCTET_STREAM))
        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFilename() + "\"")
        		.body(doc);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/images/{name}")
	public ResponseEntity<UrlResource> getImage(@PathVariable("name") String name, @RequestHeader HttpHeaders headers) throws Exception {
		
		//List<Material> materials = materialDao.selectByCourseId(courseid);
		String path = new File("").getAbsolutePath();
		String vpath = "file:///" + path +"\\"+ contentPath+"\\"+name;
		UrlResource doc = new UrlResource(vpath);
        return ResponseEntity.ok()
        		.contentType(MediaTypeFactory.getMediaType(doc).orElse(MediaType.APPLICATION_OCTET_STREAM))
        		.body(doc);
	}

	
	@RequestMapping(method = RequestMethod.GET, path = "/videoframe/{name}/{frame_no}")
	public ResponseEntity<UrlResource> getVideoFrame(@PathVariable("name") String name, @PathVariable("frame_no") int frame_num, @RequestHeader HttpHeaders headers) throws Exception {
    	String vframe = getVideoframe(name, frame_num);
    	return getImage(vframe, headers);

	}

	@RequestMapping(method = RequestMethod.GET, path = "/videos/{name}")
	public ResponseEntity<ResourceRegion> getVideo(@PathVariable("name") String name, @RequestHeader HttpHeaders headers) throws Exception {
		
		String path = new File("").getAbsolutePath();
		//String vpath = "file:///"+ path +"\\"+ contentPath+"\\"+name;
		String vpath="file:F:\\JAVA_EE_project\\OauthRestService_elearning\\E_Learning\\files\\video.mp4";
		UrlResource video = new UrlResource(vpath);
		ResourceRegion region = resourceRegion(video, headers);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(region);
	}

	
    private ResourceRegion resourceRegion(UrlResource video, HttpHeaders headers) throws Exception {
        long contentLength = video.contentLength();
        //long range = headers.range.firstOrNull();
        //long range = headers.getRange().isEmpty()
        
       HttpRange range;
       if (!headers.getRange().isEmpty()) {
    	   
    	    range = headers.getRange().get(0);
    	   
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long size = end - start + 1;
            long rangeLength = ChunkSize>size? size:ChunkSize;
            return new ResourceRegion(video, start, rangeLength);
        } else {
            long rangeLength = ChunkSize>contentLength?contentLength:ChunkSize;
            return new ResourceRegion(video, 0, rangeLength);
        }
    }

		
	private String getVideoframe(String videopath, int frame_num) throws IOException, JCodecException {
		String vframe;
		
		int frameNumber = 2;
		
		if (frame_num > 0) frameNumber = frame_num;
		
		int i =videopath.lastIndexOf('.');
		vframe = videopath.substring(0,i)+".jpg";
		
		File f = new File(vframe);
		if (!f.exists())
		{
			String path = new File("").getAbsolutePath();
			videopath = path +"\\"+ contentPath+"\\"+ videopath;
			Picture picture = FrameGrab.getFrameFromFile(new File(videopath), frameNumber);
	
			//for JDK (jcodec-javase)
			BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
			ImageIO.write(bufferedImage, "jpg", new File(path +"\\"+ contentPath+"\\"+ vframe));
		}
		return vframe;
	}
	

}
