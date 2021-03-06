package com.example.fileuploaddemo;

import com.example.fileuploaddemo.exam01.UploadModel;
import com.example.fileuploaddemo.exam03.S3FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MainController {

    private static String UPLOAD_PATH = System.getProperty("user.home") + "/exam02";

    @Autowired
    private S3FileService fileService;

    @GetMapping("/")
    public String S3Upload() {
        return "index";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        String aStatic = fileService.saveThumb(multipartFile.getInputStream(), multipartFile.getContentType(), multipartFile.getSize(), "static");
        System.out.println(aStatic);
        return aStatic;
    }

    @GetMapping("/exam01")
    public String upload() {
        return "uploadExam01";
    }

    @GetMapping("/exam02")
    public String upload2() {
        return "uploadExam02";
    }

    @PostMapping("/uploadExam02")
    public void upload (MultipartFile uploadfile, Model model){
        String result = saveFile(uploadfile);
        if(result != null) {
            model.addAttribute("result", result);
        } else {
            model.addAttribute("result", "fail");
        }
    }

    // 여러 파일을 구분해서 받기
    @PostMapping("/MultiUploadExam02")
    public String upload(MultipartFile[] myfile1,
            MultipartFile[] myfile2, Model model){

        System.out.println("=====length======");
        System.out.println("myfile1: "+ myfile1.length);
        System.out.println("myfile2: "+ myfile2.length);
        System.out.println("================");

        UploadModel uploadModel;

        String result = "";
        result = getString(myfile1, result);
        result = getString(myfile2, result);
        model.addAttribute("result",result);

        return "redirect:/exam02";
    }

    private String getString(MultipartFile[] myfile, String result) {
        for(MultipartFile f : myfile){
            System.out.println("=====getName=====");
            System.out.println(f.getName());
            System.out.println("=================");
            result += saveFile(f);
        }
        return result;
    }

    private String saveFile(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String saveName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(UPLOAD_PATH, saveName);

        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return saveName;
    }
}
