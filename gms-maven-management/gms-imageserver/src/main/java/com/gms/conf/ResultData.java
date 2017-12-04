package com.gms.conf;

import java.util.HashMap;
import java.util.Map;

public class ResultData
{
  private final int code;
  private final String message;
  private final Map<String, Object> data = new HashMap();

  public String getMessage() {
    return this.message;
  }

  public int getCode() {
    return this.code;
  }

  public Map<String, Object> getData() {
    return this.data;
  }

  public ResultData putDataValue(String key, Object value) {
    this.data.put(key, value);
    return this;
  }

  private ResultData(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ResultData ok() {
    return new ResultData(200, "Ok");
  }

  public static ResultData notFound() {
    return new ResultData(404, "Not Found");
  }

  public static ResultData badRequest() {
    return new ResultData(400, "Bad Request");
  }

  public static ResultData forbidden() {
    return new ResultData(403, "Forbidden");
  }

  public static ResultData unauthorized() {
    return new ResultData(401, "Unauthorized");
  }

  public static ResultData serverInternalError() {
    return new ResultData(500, "Server Internal Error");
  }

  public static ResultData customerError() {
    return new ResultData(1001, "Customer Error");
  }
}