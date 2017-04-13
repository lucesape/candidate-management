package com.msg;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author blajv
 * @version $Id$
 *          CLASS-HEADER
 */
@Controller
public class HomeController {
    @RequestMapping(value = "/")
    	public String index() {
    		return "index";
    	}

}
