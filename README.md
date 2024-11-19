<div align="center">
  <h1 style="display: flex; align-items: center; justify-content: center; width: 100%;">
    <img src="./resources/img/logo.png" width="22px" style="margin-right: 15px"/> 코플로, CoFlo
  </h1>
  <img src="./resources/img/main.png"/>
  <h4>코플로 서비스</h5>
  <p>2024.10.?? ~ 2024.11.19</p>  
    
  <a href="">코플로</a>
  &nbsp; | &nbsp; 
  <a href="">리뷰핑 팀 Notion</a>
  &nbsp; | &nbsp; 
   <a href="exec/최종발표.pdf">발표 PPT</a>
</div>

<br>

## 목차

1. [개요](#개요)
2. [핵심 기능 소개](#기능-소개)
3. [서비스 소개](#서비스-소개)
4. [프로젝트 설계](#프로젝트-설계)
5. [개발 환경 및 기술 스택](#개발-환경-및-기술-스택)
6. [팀원 소개](#팀원-소개)

<br/>

## [개요](#목차)

<br>
 
### **코플로란?**

<br>
<br>

## [핵심 기능 소개](#목차)

### 1.

<br/>
<br/>

## [서비스 소개](#목차)

### 1️⃣ <b>메인 페이지</b>

|                     **Login Page**                      |                        **Main Page**                        |                       **Main Page**                        |
| :-----------------------------------------------------: | :---------------------------------------------------------: | :--------------------------------------------------------: |
| <img src="./resources/gif/main_login.gif" height="400"> | <img src="./resources/gif/main_bestalbum.gif" height="400"> | <img src="./resources/gif/main_tagalbum.gif" height="400"> |

<br>

### 2️⃣ <b> 페이지</b>

> 사용자가 자신의 목소리를 녹음하여 곡을 만드는 페이지로, MR을 선택하고 바로 녹음할 수 있습니다.

|                      **Melting Page**                      |                      **AI Cover Page**                      |
| :--------------------------------------------------------: | :---------------------------------------------------------: |
| <img src="./resources/gif/music_melting.gif" height="400"> | <img src="./resources/gif/music_ai_cover.gif" height="400"> |

<br>

### 3️⃣ <b> 페이지</b>

> 완성된 커버곡과 멜팅곡을 선택하여 앨범을 발매하는 페이지입니다. 앨범 커버 이미지와 소개가 AI를 통해 자동 생성됩니다.

|                   **Album Register Page**                   |
| :---------------------------------------------------------: |
| <img src="./resources/gif/album_creation.gif" height="400"> |

<br>

### 4️⃣ <b> 페이지</b>

> 사용자들이 서로의 앨범을 공유하고 좋아요와 댓글을 남길 수 있는 커뮤니티 공간입니다.

|                     **Community Page**                      |                        **Search Page**                        |
| :---------------------------------------------------------: | :-----------------------------------------------------------: |
| <img src="./resources/gif/community_main.gif" height="400"> | <img src="./resources/gif/community_search.gif" height="400"> |

|                    **Album Detail Page**                     |                     **Album Streaming Page**                     |                     **Album Comment Page**                     |
| :----------------------------------------------------------: | :--------------------------------------------------------------: | :------------------------------------------------------------: |
| <img src="./resources/gif/community_liked.gif" height="400"> | <img src="./resources/gif/community_streaming.gif" height="400"> | <img src="./resources/gif/community_comment.gif" height="400"> |

<br>

### 5️⃣ <b>마이 페이지</b>

|                          **Info Page**                          |                      **My Album/Song Page**                       |                      **Liked Album/Song Page**                       |
| :-------------------------------------------------------------: | :---------------------------------------------------------------: | :------------------------------------------------------------------: |
| <img src="./resources/gif/mypage_member_info.gif" height="400"> | <img src="./resources/gif/mypage_my_album_song.gif" height="400"> | <img src="./resources/gif/mypage_liked_album_song.gif" height="400"> |

<br/>
<br/>

## [프로젝트 설계](#목차)

### 시스템 아키텍쳐

<img width="500px" src="./resources/img/아키텍쳐.png">

<br/>

### ERD

<img width="500px" src="./resources/img/ERD.png">

<br>

### [코플로 API 명세서](./resources/img/api_specification.png)

<br/>
<br/>

## [개발 환경 및 기술 스택](#목차)

|      개발 환경      | 기술 스택                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| :-----------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|    **Frontend**     | ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white) ![CSS](https://img.shields.io/badge/CSS-1572b6?style=for-the-badge&logo=css3&logoColor=white) ![react](https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=white) ![typescript](https://img.shields.io/badge/typescript-3178C6?style=for-the-badge&logo=typescript&logoColor=white) ![axios](https://img.shields.io/badge/axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white) ![tailwind](https://img.shields.io/badge/tailwind-css-06B6D4.svg?style=for-the-badge&logo=tailwindcss&logoColor=white) ![shadcn](https://img.shields.io/badge/shadcn/ui-000000.svg?style=for-the-badge&logo=shadcnui&logoColor=white) ![pwa](https://img.shields.io/badge/pwa-5A0FC8.svg?style=for-the-badge&logo=pwa&logoColor=white) |
|     **Backend**     | ![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.3.3-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) ![OAuth2](https://img.shields.io/badge/OAuth2-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-gray?style=for-the-badge&logo=Spring_Data_JPA&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)                                       |
|       **DB**        | ![PostgreSQL](https://img.shields.io/badge/postgresql-4479A1?style=for-the-badge&logo=postgresql&logoColor=white) ![redis](https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white) ![Elasticsearch](https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
|       **AI**        | ![fast api](https://img.shields.io/badge/FastAPI-005571?style=for-the-badge&logo=fastapi) ![huggingface](https://img.shields.io/badge/huggingface-FFD21E?style=for-the-badge&logo=huggingface&logoColor=black)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
|      **Infra**      | ![amazonec2](https://img.shields.io/badge/amazon_ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) ![docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![nginx](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| **Management Tool** | ![Jira](https://img.shields.io/badge/jira-0052CC?style=for-the-badge&logo=jira&logoColor=white) ![Gitlab](https://img.shields.io/badge/GitLab-FC6D26?style=for-the-badge&logo=GitLab&logoColor=white) ![Mattermost](https://img.shields.io/badge/mattermost-0058CC?style=for-the-badge&logo=mattermost&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000.svg?style=for-the-badge&logo=notion&logoColor=white) ![Figma](https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white) ![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)                                                                                                                                                                                                              |

<br/>

<details>
<summary>리뷰핑팀 JIRA</summary>
<div markdown="1">
<img width="500px" src="./resources/img/jira/jira_flow_digram.PNG">
</div>
</details>

<br/>
<br/>

## [팀원 소개](#목차)

<table align="center">
  <tr>
    <th style="text-align: center;"><a href="https://github.com/anjs124">구승석</a></th>
    <th style="text-align: center;"><a href="https://github.com/btothey99">이보연</a></th>
    <th style="text-align: center;"><a href="https://github.com/hyooun">유승현</a></th>
  </tr>
  <tr>
    <td style="text-align: center;"><img src="./resources/img/member/구승석.png" alt="" width="150px"/></td>
    <td style="text-align: center;"><img src="./resources/img/member/이보연.png" alt="" width="150px"/></td>
    <td style="text-align: center;"><img src="./resources/img/member/유승현.png" alt="" width="150px" /></td>
  </tr>
  <tr>
    <td style="text-align: center;"><b>Frontend</b></td>
    <td style="text-align: center;"><b>Frontend</b></td>
    <td style="text-align: center;"><b>Infra/Backend</b></td>
  </tr>
</table>

<table align="center">
  <tr>
    <th style="text-align: center;"><a href="https://github.com/jmxx219">손지민</a></th>
    <th style="text-align: center;"><a href="https://github.com/fkgnssla">김형민</a></th>
    <th style="text-align: center;"><a href="https://github.com/qsunki">홍선기</a></th>
  </tr>
  <tr>
    <td style="text-align: center;"><img src="./resources/img/member/손지민.png" alt="" width="150px" /></td>
    <td style="text-align: center;"><img src="./resources/img/member/김형민.png" alt="" width="150px"/></td>
    <td style="text-align: center;"><img src="./resources/img/member/홍선기.png" alt="" width="150px" /></td>
  </tr>
  <tr>
    <td style="text-align: center;"><b>Backend</b></td>
    <td style="text-align: center;"><b>Backend</b></td>
    <td style="text-align: center;"><b>Backend/AI</b></td>
  </tr>
</table>

<br>
