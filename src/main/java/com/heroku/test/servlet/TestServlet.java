package com.heroku.test.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TestServlet extends HttpServlet {
     
    
    private class CountHolder implements Serializable {

        private static final long serialVersionUID = 1L;
        private Integer count;
        private Date time;

        public CountHolder() {
            count = 0;
            time = new Date();
        }
        
        public Integer getCount() {
            return count;
        }
        
        public void plusPlus() {
            count++;
        }
        
        public void setTime(Date time) {
            this.time = time;
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        CountHolder count;
        
        if(session.getAttribute("count") != null) {            
            count = (CountHolder) session.getAttribute("count");
        } else {
            count = new CountHolder();
        }
        
        count.setTime(new Date());
        count.plusPlus();
        
        System.out.println("Count: " + count.getCount());
        
        session.setAttribute("count", count);
        
        resp.getWriter().print("count = " + count.getCount());
    }

}
