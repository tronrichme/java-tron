package org.tron.core.services.http;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.common.utils.Time;
import org.tron.core.Wallet;
import org.tron.protos.Protocol.Block;


@Component
@Slf4j(topic = "API")
public class GetNowBlockServlet extends HttpServlet {

  @Autowired
  private Wallet wallet;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    long start = Time.getCurrentMillis();
    try {
      Block reply = wallet.getNowBlock();
      if (reply != null) {
        long time1 = System.currentTimeMillis();
        String s= Util.printBlock(reply);
        long time2 = System.currentTimeMillis();
        response.getWriter().println(s);
        logger.info("GetNowBlockServlet duration: getNowBlock={}, printBlock={}, response={}, size:{}, address:{}",
            time1 - start, time2 - time1, Time.getCurrentMillis() - time2, s.length(), request.getRemoteAddr());
      } else {
        response.getWriter().println("{}");
      }

    } catch (Exception e) {
      logger.debug("Exception: {}", e.getMessage());
      try {
        response.getWriter().println(Util.printErrorMsg(e));
      } catch (IOException ioe) {
        logger.debug("IOException: {}", ioe.getMessage());
      }
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    doGet(request, response);
  }
}