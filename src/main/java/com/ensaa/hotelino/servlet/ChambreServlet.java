package com.ensaa.hotelino.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ensaa.hotelino.database.ConnexionBD;
import com.ensaa.hotelino.entity.Chambre;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/chambres")
public class ChambreServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int count = 0;

        Connection cn = null;
        try {
            cn = ConnexionBD.get();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM chambre");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                count ++;
            }

            System.out.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Count : " + count);
    }
}
