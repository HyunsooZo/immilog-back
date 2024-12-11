package com.backend.immilog.user.enums;

public class EmailComponents {
    public static final String
            EMAIL_SIGN_UP_SUBJECT = "Immilog 회원가입 인증 메일",
            API_LINK = "https://api.ko-meet-back.com/api/v1/users/%d/verification",
            PASSWORD_RESET_SUBJECT = "Immilog 비밀번호가 초기화 되었습니다.",
            PASSWORD_RESET_CONTENT = "아래 임시 비밀번호를 통해 로그인 후 비밀번호를 변경해주세요.\n 임시비밀번호 : %s",
            HTML_SIGN_UP_CONTENT =
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                            "  <head>\n" +
                            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\" />\n" +
                            "    <title>koMeet</title>\n" +
                            "  </head>\n" +
                            "  <body>\n" +
                            "    <table\n" +
                            "      width=\"100%%\"\n" +
                            "      cellspacing=\"0\"\n" +
                            "      cellpadding=\"0\"\n" +
                            "      align=\"center\"\n" +
                            "      border=\"0\"\n" +
                            "      bgcolor=\"#ffffff\"\n" +
                            "      style=\"\n" +
                            "        max-width: 600px;\n" +
                            "        margin: 0 auto 0 auto;\n" +
                            "        font-family:\n" +
                            "          Helvetica Neue,\n" +
                            "          Helvetica,\n" +
                            "          Lucida Grande,\n" +
                            "          tahoma,\n" +
                            "          verdana,\n" +
                            "          arial,\n" +
                            "          sans-serif;\n" +
                            "        border-collapse: collapse;\n" +
                            "      \"\n" +
                            "    >\n" +
                            "      <tbody>\n" +
                            "        <tr>\n" +
                            "          <td height=\"20\" style=\"line-height: 20px\">&nbsp;</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "          <td>\n" +
                            "            <table\n" +
                            "              width=\"100%%\"\n" +
                            "              cellspacing=\"0\"\n" +
                            "              cellpadding=\"0\"\n" +
                            "              border=\"0\"\n" +
                            "              style=\"border-collapse: collapse\"\n" +
                            "            >\n" +
                            "              <tbody>\n" +
                            "                <tr>\n" +
                            "                  <td width=\"10\" style=\"width: 10px\">&nbsp;</td>\n" +
                            "                  <td align=\"left\" style=\"line-height: 0px\">\n" +
                            "                    <img\n" +
                            "                      src=\"https://raw.githubusercontent.com/ko-meet/ko-meet-front/main/src/assets/images/email-icon-logo.png\"\n" +
                            "                      alt=\"komeet\"\n" +
                            "                      width=\"90\"\n" +
                            "                      height=\"23\"\n" +
                            "                      border=\"0\"\n" +
                            "                      style=\"display: block\"\n" +
                            "                    />\n" +
                            "                  </td>\n" +
                            "                  <td width=\"10\" style=\"width: 10px\">&nbsp;</td>\n" +
                            "                </tr>\n" +
                            "              </tbody>\n" +
                            "            </table>\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "          <td height=\"20\" style=\"line-height: 20px\">&nbsp;</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "          <td>\n" +
                            "            <table\n" +
                            "              width=\"100%%\"\n" +
                            "              cellspacing=\"0\"\n" +
                            "              cellpadding=\"0\"\n" +
                            "              align=\"center\"\n" +
                            "              border=\"0\"\n" +
                            "              style=\"max-width: 450px; border-collapse: collapse\"\n" +
                            "            >\n" +
                            "              <tbody>\n" +
                            "                <!-- message -->\n" +
                            "                <tr>\n" +
                            "                  <td width=\"20\" style=\"width: 20px\">&nbsp;</td>\n" +
                            "                  <td>\n" +
                            "                    <table\n" +
                            "                      width=\"100%%\"\n" +
                            "                      cellspacing=\"0\"\n" +
                            "                      cellpadding=\"0\"\n" +
                            "                      border=\"0\"\n" +
                            "                      style=\"border-collapse: collapse\"\n" +
                            "                    >\n" +
                            "                      <tbody>\n" +
                            "                        <tr>\n" +
                            "                          <td style=\"color: #4e5f70; font-size: 14px; line-height: 20px\">\n" +
                            "                            <span " + "class=\"userName" + "\">%s</span>님, Immilog를 이용해 주셔서 감사합니다.\n" +
                            "                          </td>\n" +
                            "                        </tr>\n" +
                            "                        <tr>\n" +
                            "                          <td height=\"5\" style=\"line-height: 5px\">&nbsp;</td>\n" +
                            "                        </tr>\n" +
                            "                        <tr>\n" +
                            "                          <td style=\"color: #4e5f70; font-size: 14px; line-height: 20px\">\n" +
                            "                            아래 버튼을 클릭하시면 회원가입 인증이 완료 됩니다.\n" +
                            "                          </td>\n" +
                            "                        </tr>\n" +
                            "                        <tr>\n" +
                            "                          <td height=\"20\" style=\"line-height: 20px\">&nbsp;</td>\n" +
                            "                        </tr>\n" +
                            "                        <tr>\n" +
                            "                          <td align=\"center\">\n" +
                            "                            <a\n" +
                            "                              href=\"%s\"\n" +
                            "                              target=\"_blank\"\n" +
                            "                              style=\"\n" +
                            "                                display: block;\n" +
                            "                                max-width: 250px;\n" +
                            "                                padding-top: 10px;\n" +
                            "                                padding-bottom: 10px;\n" +
                            "                                color: #fdfdfd;\n" +
                            "                                border-radius: 6px;\n" +
                            "                                border: 1px solid #292f33;\n" +
                            "                                color: #292f33;\n" +
                            "                                text-decoration: none;\n" +
                            "                                font-weight: bold;\n" +
                            "                                font-size: 14px;\n" +
                            "                                line-height: 18px;\n" +
                            "                              \"\n" +
                            "                            >\n" +
                            "                              회원가입&nbsp;인증&nbsp;완료\n" +
                            "                            </a>\n" +
                            "                          </td>\n" +
                            "                        </tr>\n" +
                            "                      </tbody>\n" +
                            "                    </table>\n" +
                            "                  </td>\n" +
                            "                  <td width=\"20\" style=\"width: 20px\">&nbsp;</td>\n" +
                            "                </tr>\n" +
                            "              </tbody>\n" +
                            "            </table>\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "          <td height=\"20\" style=\"line-height: 20px\">&nbsp;</td>\n" +
                            "        </tr>\n" +
                            "      </tbody>\n" +
                            "    </table>\n" +
                            "  </body>\n" +
                            "</html>\n";

}
