package hq.com.control;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hq.com.aop.utils.DateUtils;
import hq.com.aop.utils.ExcelParam;
import hq.com.aop.utils.ExcelUtils;
import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.OutParam;
import hq.com.enums.SystemCodeEnum;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/file")
public class FileController {
	Logger logger  =  Logger.getLogger(FileController.class);

	private final String path = "g:/";

	/**
	 * 测试生成Excel文件流至客户端
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/excel",method={RequestMethod.POST,RequestMethod.GET})
	public void excel(HttpServletResponse response) throws IOException {
		ExcelUtils.WriteExcel we = new ExcelUtils.WriteExcel();
		ExcelParam ep = new ExcelParam();
		List<ExcelParam.WriteParam> sheets = new ArrayList<>();
		ExcelParam.WriteParam sheet = ep.new WriteParam();
		sheet.setSheetHead("测试");
		sheet.setSheetName("超过规定数据");
		List<String> title = new ArrayList<>();
		title.add("呵呵1");
		title.add("呵呵2");
		title.add("呵呵3");
		title.add("呵呵4");
		title.add("呵呵5");
		title.add("呵呵6");
		sheet.setTitles(title);
		List<String> others = new ArrayList<>();
		others.add("fuck:sdfsdf");
		others.add("fuck:sdfsdf");
		others.add("fuck:sdfsdf");
		sheet.setOthers(others);
		Map<String,Object> m1 = new HashMap<>();
		m1.put("t1","12");
		m1.put("t2","sdasdfds");
		m1.put("t3","中文");
		m1.put("t4",123);
		m1.put("t5",new Date());
		m1.put("t6",123.9910203);
		List<Map<String,Object>> body = new ArrayList<>();
		body.add(m1);
		body.add(m1);
		body.add(m1);
		body.add(m1);
		body.add(m1);
		sheet.setBody(body);
		sheets.add(sheet);
		we.buildStream(response).write(sheets,ep.new LoadSheet<Map<String,Object>>(){
			@Override
			public String[] loadBody(Map<String, Object> obj, int titleLength) {
				String [] objs = new String[titleLength];
				objs[0]=obj.get("t1")+"";
				objs[1]=obj.get("t2")+"";
				objs[2]=obj.get("t3")+"";
				objs[3]=obj.get("t4")+"";
				objs[4]=obj.get("t5")+"";
				objs[5]=obj.get("t6")+"";
				return objs;
			}

			@Override
			public CellStyle bodyStyle(Workbook wb) {
				CellStyle cellStyle = wb.createCellStyle();
				Font font = wb.createFont();
				font.setFontName("宋体");
				font.setBold(false);//粗体显示
				cellStyle.setFont(font);
				cellStyle.setWrapText(true);
				return cellStyle;
			}

			@Override
			public CellStyle titleStyle(Workbook wb) {
				return null;
			}
		});
	}

	/**
	 * 上传文件
	 *
	 * @description 支持批量上传,实际上传控件是一个个上传
	 * @param files
	 */
	@RequestMapping(value="/upload",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public OutParam fileupload(@RequestParam(value="fileData") MultipartFile [] files){
		OutParam op = new OutParam();
		List<Map<String,Object>> list = new ArrayList<>();
		try {
		     String date = DateUtils.dateToString(new Date(),DateUtils.YYYYMMDD);
		     if (StringUtils.isNotEmpty(files)){
				 for (MultipartFile file : files) {
					 StringBuffer newFileName = new StringBuffer();
					 newFileName.append(date).
							 append(StringUtils.SLASH).
							 append(UUID.randomUUID().toString().replaceAll(StringUtils.RAIL, "")).
							 append(StringUtils.subEnd(file.getOriginalFilename(),StringUtils.POINT));
					 File tmplFile = new  File(path+date);
					 if (!tmplFile.exists()) {
						 tmplFile.mkdirs();
					 }
					 if(logger.isInfoEnabled()){
						 logger.info("上传文件("+newFileName.toString()+")至本地服务器开始时间："+DateUtils.dateToString(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS));
					 }
					 file.transferTo(new File(path+newFileName));
					 if(logger.isInfoEnabled()){
						 logger.info("上传文件("+newFileName+")至本地服务器结束时间："+DateUtils.dateToString(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS));
					 }
					 Map<String,Object> data = new HashMap<>();
					 data.put("name",newFileName);
					 data.put("size",file.getSize());
					 list.add(data);
				 }
				 op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
				 op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
				 op.setContent(list);
			 }
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
		    	logger.info(" 调用方法(upload.do)上传文件抛出异常信息:["+e.getMessage()+"]");
		    }
			op.setCode(SystemCodeEnum.SYSTEM_ERROR.getCode());
			op.setDesc(SystemCodeEnum.SYSTEM_ERROR.getDesc());
		}
		return op;
	}
	
	/**
	 * 下载文件
	 * 
	 * @param url
	 *           下载路径
	 * @param response
	 */
	@RequestMapping(value="/download",method={RequestMethod.POST,RequestMethod.GET})
	public void downloadAppApk(@RequestParam("url") String url,HttpServletRequest request,HttpServletResponse response){
		try {
			url = URLDecoder.decode(url,"utf-8");
			url = URLDecoder.decode(url,"utf-8");
			File file = new File(path+url);
			
			InputStream inputStream = new FileInputStream(file);
			OutputStream outputStream = response.getOutputStream();
			response.setHeader("content-disposition", "attachment;filename="+new String(file.getName().getBytes("UTF-8"), "ISO8859_1"));
			response.setHeader("content-length", String.valueOf(file.length()));
			response.setContentType("multipart/form-data");
			response.setCharacterEncoding("UTF-8");
			byte [] b = new byte[1024];
			int length = 0;
			while((length=inputStream.read(b))!=-1){
				outputStream.write(b,0,length);
			}
			if(null!=inputStream){
				inputStream.close();
			}
			if(logger.isInfoEnabled()){
				logger.info(" 调用方法(download.do)下载文件输入参数:[下载路径(url):"+url+"]");
		    	logger.info(" 调用方法(download.do)下载文件输出信息:[下载成功]");
		    }
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
		    	logger.info(" 调用方法(download.do)下载文件抛出异常信息:["+e.getMessage()+"]");
		    }
		}
	}

	public static void main(String[] args) {
		String str = "sdf.s.df.png";
		System.out.println(str.lastIndexOf("."));
		System.out.println(str.substring(str.lastIndexOf(".")));
	}
}
