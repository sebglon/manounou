/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-10-28 17:08:27 UTC)
 * on 2014-11-06 at 12:30:22 UTC 
 * Modify at your own risk.
 */

package com.appspot.mananou44sql.timesheetApi.model;

/**
 * Model definition for Timesheet.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the timesheetApi. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Timesheet extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String contract;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<DailyLog> dailyLogs;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String encodedKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private MonthDay monthDay;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String status;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<StatusHistory> statusHistory;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getContract() {
    return contract;
  }

  /**
   * @param contract contract or {@code null} for none
   */
  public Timesheet setContract(java.lang.String contract) {
    this.contract = contract;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<DailyLog> getDailyLogs() {
    return dailyLogs;
  }

  /**
   * @param dailyLogs dailyLogs or {@code null} for none
   */
  public Timesheet setDailyLogs(java.util.List<DailyLog> dailyLogs) {
    this.dailyLogs = dailyLogs;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getEncodedKey() {
    return encodedKey;
  }

  /**
   * @param encodedKey encodedKey or {@code null} for none
   */
  public Timesheet setEncodedKey(java.lang.String encodedKey) {
    this.encodedKey = encodedKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public MonthDay getMonthDay() {
    return monthDay;
  }

  /**
   * @param monthDay monthDay or {@code null} for none
   */
  public Timesheet setMonthDay(MonthDay monthDay) {
    this.monthDay = monthDay;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getStatus() {
    return status;
  }

  /**
   * @param status status or {@code null} for none
   */
  public Timesheet setStatus(java.lang.String status) {
    this.status = status;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<StatusHistory> getStatusHistory() {
    return statusHistory;
  }

  /**
   * @param statusHistory statusHistory or {@code null} for none
   */
  public Timesheet setStatusHistory(java.util.List<StatusHistory> statusHistory) {
    this.statusHistory = statusHistory;
    return this;
  }

  @Override
  public Timesheet set(String fieldName, Object value) {
    return (Timesheet) super.set(fieldName, value);
  }

  @Override
  public Timesheet clone() {
    return (Timesheet) super.clone();
  }

}
