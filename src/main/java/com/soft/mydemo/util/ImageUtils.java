package com.soft.mydemo.util;

import com.spire.doc.Document;
import com.spire.doc.documents.DocumentObjectType;
import com.spire.doc.interfaces.ICompositeObject;
import com.spire.doc.interfaces.IDocumentObject;
import com.microsoft.schemas.vml.CTShape;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 图片处理工具
 *
 * @author feimingz
 * @date 2021/06/18
 */
public class ImageUtils {

    /**
     * 校验文件中是否含有图片
     *
     * @param multipartFile 文件内容
     * @return 是否含有图片
     */
    public static boolean checkIfExistImagesFromWord(MultipartFile multipartFile) throws Exception {
        File file = multipartFileToFile(multipartFile);
        // 加载Word文档
        Document document = new Document();
        document.loadFromFile(String.valueOf(file));

        // 创建Queue对象
        Queue nodes = new LinkedList();
        nodes.add(document);

        // 创建List对象
        List images = new ArrayList();

        // 遍历文档中的子对象
        while (nodes.size() > 0) {
            ICompositeObject node = (ICompositeObject) nodes.poll();
            for (int i = 0; i < node.getChildObjects().getCount(); i++) {
                IDocumentObject child = node.getChildObjects().get(i);
                if (child instanceof ICompositeObject) {
                    nodes.add(child);
                    // 获取图片并添加到List
                    if (child.getDocumentObjectType() == DocumentObjectType.Picture) {
                        deleteTempFile(file);
                        return true;
                        /*DocPicture picture = (DocPicture) child;
                        images.add(picture.getImage());*/
                    }
                }
            }
        }
        deleteTempFile(file);
        return false;
    }

    //检查word文件中是否有图片方式二：POI
    public static boolean checkWordFileHasImgs(MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName.endsWith(".doc")) {
            HWPFDocument doc = new HWPFDocument(multipartFile.getInputStream());
            PicturesTable picturesTable = doc.getPicturesTable();
            List<Picture> allPictures = picturesTable.getAllPictures();
            return allPictures.size() > 0;
        } else {
            XWPFDocument xwpfDocument = new XWPFDocument(multipartFile.getInputStream());
            List<XWPFParagraph> paragraphList = xwpfDocument.getParagraphs();
            if (paragraphList == null) return false;
            return checkDocxFileHasImgs(paragraphList);
        }
    }

    //检查docx文件中是否有图片
    private static boolean checkDocxFileHasImgs(List<XWPFParagraph> paragraphList) {
        for (XWPFParagraph paragraph : paragraphList) {
            //段落中所有XWPFRun
            List<XWPFRun> runList = paragraph.getRuns();
            for (XWPFRun run : runList) {
                //XWPFRun是POI对xml元素解析后生成的自己的属性，无法通过xml解析，需要先转化成CTR
                CTR ctr = run.getCTR();
                //对子元素进行遍历
                XmlCursor c = ctr.newCursor();
                //这个就是拿到所有的子元素：
                c.selectPath("./*");
                while (c.toNextSelection()) {
                    XmlObject o = c.getObject();
                    //如果子元素是<w:drawing>这样的形式，使用CTDrawing保存图片
                    if (o instanceof CTDrawing) {
                        CTDrawing drawing = (CTDrawing) o;
                        CTInline[] ctInlines = drawing.getInlineArray();
                        for (CTInline ctInline : ctInlines) {
                            CTGraphicalObject graphic = ctInline.getGraphic();
                            //
                            XmlCursor cursor = graphic.getGraphicData().newCursor();
                            cursor.selectPath("./*");
                            while (cursor.toNextSelection()) {
                                XmlObject xmlObject = cursor.getObject();
                                // 如果子元素是<pic:pic>这样的形式
                                if (xmlObject instanceof CTPicture) {
                                    return true;
                                }
                            }
                        }
                    }
                    //使用CTObject保存图片 //<w:object>形式
                    if (o instanceof CTObject) {
                        CTObject object = (CTObject) o;
                        XmlCursor w = object.newCursor();
                        w.selectPath("./*");
                        while (w.toNextSelection()) {
                            XmlObject xmlObject = w.getObject();
                            if (xmlObject instanceof CTShape) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * MultipartFile 转 File
     *
     * @param file 文件信息
     * @throws Exception 异常
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {
        if (StringUtils.isEmpty(file) || file.getSize() <= 0) {
            return null;
        }
        InputStream ins = file.getInputStream();
        File toFile = new File(file.getOriginalFilename());
        inputStreamToFile(ins, toFile);
        ins.close();
        return toFile;
    }


    // 获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file 文件
     */
    public static void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }
}

