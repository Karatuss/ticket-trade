<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the ticket-trade and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Thanks again! Now go create something AMAZING! :D
***
***
***
*** To avoid retyping too much info. Do a search and replace for the following:
*** TwoPair, ticket-trade, twitter_handle, shm1193@gmail.com, Ticket Trade, 캡스톤 디자인 프로젝트 (이후 내용 추가하기)
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/TwoPair/ticket-trade">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Ticket Trade</h3>

  <p align="center">
    Anybody can create a ticket trade for his/her own event with preventing from scalped tickets.
    <br />
    <a href="https://github.com/TwoPair/ticket-trade"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <!-- 나중에 데모 사이트 같은거 만들어서 보여주는 것도 나쁘지 않아보임. 링크 수정도 필요함. -->
    <a href="https://github.com/TwoPair/ticket-trade">View Demo</a>
    ·
    <a href="https://github.com/TwoPair/ticket-trade/issues">Report Bug</a>
    ·
    <a href="https://github.com/TwoPair/ticket-trade/issues">Request Feature</a>
  </p>




<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://example.com)

Here's a blank template to get started:
**To avoid retyping too much info. Do a search and replace with your text editor for the following:**
`TwoPair`, `ticket-trade`, `twitter_handle`, `shm1193@gmail.com`, `Ticket Trade`, `캡스톤 디자인 프로젝트 (이후 내용 추가하기)`


### Built With

* []()
* []()
* []()



<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

Please follow the official hyperledger-fabric document.
[https://hyperledger-fabric.readthedocs.io/en/latest/prereqs.html](https://hyperledger-fabric.readthedocs.io/en/latest/prereqs.html)

- Hyperledger fabric 2.5.0
- Docker Desktop 4.23.0
- Spring boot 3.1.4
- **여타 다른거 추가하기**

#### CouchBase Setting

- Docker
```
docker run -d --name db -p 8091-8094:8091-8094 -p 11210:11210 couchbase:community-6.5.0
```

- IntelliJ IDEA
  - Run > Edit Configurations > Modify Options > ☑️ Add VM options 
  - INPUT
    ```
    -Dcom.sun.management.jmxremote.port=9000 \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false
    ```

### Installation

1. Clone the ticket-trade
   ```sh
   git clone https://github.com/TwoPair/ticket-trade.git
   ```
2. Run the launcher **아직 안 만들었다. 전체 프로그램 동작시킬 런처 만들기!**
   ```sh
   ./launcher
   ```



<!-- USAGE EXAMPLES -->
## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

<!-- 위키도 구성해보면 좋을 듯 -->
_For more examples, please refer to the [Documentation](https://example.com)_



<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/TwoPair/ticket-trade/issues) for a list of proposed features (and known issues).



<!-- CONTRIBUTING -->
<!-- 꼭 필요할까 -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Hyunmin Shin - shm1193@gmail.com
Youngrae Kim - kyrae604@naver.com
Sooeung Im   - dlatndmd7@naver.com

Project Link: [https://github.com/TwoPair/ticket-trade](https://github.com/TwoPair/ticket-trade)



<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements

* []()
* []()
* []()





<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/TwoPair/ticket-trade.svg?style=for-the-badge
[contributors-url]: https://github.com/TwoPair/ticket-trade/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/TwoPair/ticket-trade.svg?style=for-the-badge
[forks-url]: https://github.com/TwoPair/ticket-trade/network/members
[stars-shield]: https://img.shields.io/github/stars/TwoPair/ticket-trade.svg?style=for-the-badge
[stars-url]: https://github.com/TwoPair/ticket-trade/stargazers
[issues-shield]: https://img.shields.io/github/issues/TwoPair/ticket-trade.svg?style=for-the-badge
[issues-url]: https://github.com/TwoPair/ticket-trade/issues
[license-shield]: https://img.shields.io/github/license/TwoPair/ticket-trade.svg?style=for-the-badge
[license-url]: https://github.com/TwoPair/ticket-trade/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/TwoPair
