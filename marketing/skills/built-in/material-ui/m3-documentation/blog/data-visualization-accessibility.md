---
title: "Top Tips for Data Accessibility"
source_url: "https://m3.material.io/blog/data-visualization-accessibility"
lastmod: "2024-12-23"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/a37873dce3be-mtoOMJ1gebE6i4qXKxXgi4Cyn-Qt8FinbBZGk_Jhha-NhJGvd9AX71JUmgGLIzXc1NkxLSbEjNqx4QvcIbtO5CZG5-H_yxHw.png"
  - "assets/images/6abdf63a8c1e-xSf4_TQ6-6mmp3Lr39LA8h_ACxzmOI9reyXxaE9xgRUF0UORkjZDH5tCpY8M7WzdSpAkeBw9rSrPdl5XFdWEyeKbBgSZDl4p.png"
  - "assets/images/6b657b130241-6Lr_nGKyo1qB_Dm2C5w51eOcoUfHlAqIQWkSvsRTXJlbcWvcdC13c0SlXyN880i1tV6KSM4Nt4AxuHUSOfKtG2ygWPtUGakb.png"
  - "assets/images/43492618862e-n2L87evsGnVgnHWzJ5ZNZpSmzgrPOtpFUTP1lTUsxD3Uk0t_1gNU_kUq0k-tZRM0efdgFUvQznn5nSbM1kArJhLwSwSwnW0n.png"
  - "assets/images/1c69ef02f801-tBesw7PNqbmp6fMlwDI8pTAmqTb69Et1VOiI2tzK0FhFriTrmgMeWUQYLdd2r4_j_iacWNwKi1ZMDIDAzRSQhJERDhAq2b0y.png"
  - "assets/images/3ef38e6e26cf-7YMcseoC0F5r14hOYRhxx5REP9efZb8urY_juQ6M0NvQnUV-IMWpqyjCgHz71d-mhMR4_Mfi9NhU_N24t4h5z3Iq4XtaSPnP.png"
  - "assets/images/b593bb854e17-yBmnmPX96Chf-7bBAMqIHibgC1F_Ts19CB6jg5_JT6sh0aN38RDQj29tHH01BftEFRNcsiglLAqNRv5dj56HfFqUbzfABjWN.png"
---

# Top Tips for Data Accessibility

Six strategies for making your data visualization accessible, scalable, and helpful

![Abstract illustration of various charts](<https://lh3.googleusercontent.com/mtoOMJ1gebE6i4qXKxXgi4Cyn-Qt8FinbBZGk_Jhha-NhJGvd9AX71JUmgGLIzXc1NkxLSbEjNqx4QvcIbtO5CZG5-H_yxHwDUIeBIwBF6baeFpY8so>)

Published: 2022-11-08T13:00:00

Data visualizations like charts should work for everyone, regardless of their ability. But creating accessible data visualizations extends beyond simply meeting compliance requirements. Explore the six tips below to learn how to represent data in a way that is accessible, scalable, and helpful.

See the Material Design [data visualization principles](https://material.io/design/communication/data-visualization.html#principles) for general guidance on portraying information in graphical form.

## 1. Facilitate comparisons

Arrange or convey your data so that it’s easy to compare data sets or data points across multisensory experiences including audio, haptic, and visual representations.

![Abstract example of how comparisons are facilitated through visual encodings and sonification.](<https://lh3.googleusercontent.com/xSf4_TQ6-6mmp3Lr39LA8h_ACxzmOI9reyXxaE9xgRUF0UORkjZDH5tCpY8M7WzdSpAkeBw9rSrPdl5XFdWEyeKbBgSZDl4pHe9J6ku5HNP-xIRCuA>)

**In practice:**

-   Reduce the amount of information that people need to process to understand how data sets differ or relate to each other. Keep comparable data sets on the same scales and measures where possible.
-   Provide inclusive controls like filtering  to allow people to quickly locate outliers or other instances where the data deviates from the common trend.
-   Whenever possible, use a [chart type](https://material.io/design/communication/data-visualization.html#types) that makes it easier to differentiate between data sets. For example, line charts make it easy to spot small changes over time with continuous data.

## 2\. Be a helpful guide

Provide the right context at the right time. Build affordances that prioritize data comprehension, exploration, and navigation.

![An example of how text, data ink, and data set navigation can work together to guide the user to take necessary actions.](<https://lh3.googleusercontent.com/6Lr_nGKyo1qB_Dm2C5w51eOcoUfHlAqIQWkSvsRTXJlbcWvcdC13c0SlXyN880i1tV6KSM4Nt4AxuHUSOfKtG2ygWPtUGakbXeeEpMrjMyRXEeiX1A>)

**In practice:**

-   If possible, provide a summary of what your chart is trying to convey. Refresh this based on interaction, such as if data was filtered. Try not to rely on verbally summarizing the chart’s visual elements, which can distract from the main data points.
-   Orient people within the data by consistently highlighting where they are, where they came from, and where they can go. For example, supply information on data recency and data collection methods, and standardize patterns and components across different data visualization projects whenever possible.
-   Offer flexible ways to explore and prioritize data so that people using assistive technology can manipulate the data using familiar tools (for example, through sorting).
-   Provide an accessible method for linking to the underlying data set so people can explore the data on their own (for example, link to downloadable data in CSV format or in an accessible table).

## 3. Focus on what matters

Always prioritize data accuracy, integrity, and simplicity. Every action, color, haptic, and audio element should support data insights, build understanding, and reduce cognitive load.

![A categorical chart sketch that uses outlines and fills to draw focus.](<https://lh3.googleusercontent.com/n2L87evsGnVgnHWzJ5ZNZpSmzgrPOtpFUTP1lTUsxD3Uk0t_1gNU_kUq0k-tZRM0efdgFUvQznn5nSbM1kArJhLwSwSwnW0nKMZzO9O9ighnEZ-Cz2Y>)

**In practice:**

-   Use common charts, such as area charts, bar charts, donut charts, line charts, and [other charts](https://material.io/design/communication/data-visualization.html#types) that most people already understand.
-   Use colors that achieve required  contrast ratios with adjacent elements (for example, background, metrics, and interaction states) along with an additional element or encoding to draw focus and communicate consistent meaning.
-   Ensure the visual elements and encodings within your data visualization accurately represent the underlying data.

## 4\. Provide structure

Hierarchy and structure make it easy to understand what the chart is for and what its elements represent. Appropriate structure can help users navigate your visualization.

![A representation of keyboard navigation patterns and ARIA roles and landmarks for charts.](<https://lh3.googleusercontent.com/tBesw7PNqbmp6fMlwDI8pTAmqTb69Et1VOiI2tzK0FhFriTrmgMeWUQYLdd2r4_j_iacWNwKi1ZMDIDAzRSQhJERDhAq2b0y831FO-aNyRiuGZbnmg>)

**In practice:**

-   Provide labels for legends, data points, axes, and marks so that chart elements can be unambiguously interpreted.
-   For interactive charts, use proper accessibility markup, like [ARIA](https://www.w3.org/TR/wai-aria-1.1/), to describe the organization of the data and the elements of the visualization. This will help people relying on assistive technology to interpret the visualization.
-   Use elements that support keyboard navigation (or ensure it works on your custom elements), in a way that isn’t taxing (for example, decreasing tab stops for people with limited motor skills). See the [Material accessibility principles](https://m3.material.io/foundations/accessible-design/overview) for more tips on keyboard navigation.

## 5\. Embrace flexibility

Respect different needs on data depth, complexity, and modality. Allow your data visualizations to extend and adapt over time.

![Sketch showing how a chart can be displayed on multiple devices and surfaces.](<https://lh3.googleusercontent.com/7YMcseoC0F5r14hOYRhxx5REP9efZb8urY_juQ6M0NvQnUV-IMWpqyjCgHz71d-mhMR4_Mfi9NhU_N24t4h5z3Iq4XtaSPnPplIGJtOBmyJv0hFg4Q>)

**In practice:**

-   Consider how chart elements (color palettes, axes, etc.) meet a variety of needs, abilities, screen sizes, and data types.
-   If appropriate, provide discoverable  options that allow people to choose  the visualization’s theme, contrast, and density based on their needs.

## 6\. Exceed expectations

Embrace dynamic, smart, and clever experiences that overdeliver for your audience.

![Example of how data trends can be represented by combining text, audio, and haptic feedback.](<https://lh3.googleusercontent.com/yBmnmPX96Chf-7bBAMqIHibgC1F_Ts19CB6jg5_JT6sh0aN38RDQj29tHH01BftEFRNcsiglLAqNRv5dj56HfFqUbzfABjWNxLvu2vEfUxC_sUg2hFY>)

**In practice:** 

-   Strive to create a multisensory experience that leverages a device's core capabilities, including audio, haptic, and visual representations.
-   Account for overall performance and compatibility with all platforms and assistive technology.
-   Factor in polish, surprise, and innovation as you design your data visualizations or data experiences.

## Applying this guidance

Creating accessible data experiences requires listening, humility, and a constant appetite for improvement. We should always embrace diversity on our teams and co-design our data accessibility solutions with people with varying abilities.

**These tips are just a starting point.** How we practice them will change time and time again as we continue to work together to make data experiences that provide value and insights to everyone.

#### Recommended reading

-   [Alt text guidelines](https://m3.material.io/foundations/accessible-design/overview)
-   [Google’s Six Principles for Designing Any Chart](https://medium.com/google-design/redefining-data-visualization-at-google-9bdcf2e447c6)
-   [W3C Fundamentals of Accessibility and Inclusion](https://www.w3.org/WAI/fundamentals/accessibility-usability-inclusion/)
-   [WCAG (Web Content Accessibility Guidelines)](https://www.w3.org/WAI/standards-guidelines/wcag/)

#### Acknowledgements

I’m incredibly proud of the designers, researchers, engineers, writers, and subject matter experts who helped create these tips: Brandon Wilson, Gerrit de Vries, Ian Hill, Jialin Yun, Jennifer Reilly, Jessica Klos, Jennifer Yuchi, Jesse Kulp,  Jesse Zackery, Kai Chang, Lorraine Kan, Nicholas Cottrell, Sierra Seeborn, Tyler Williamson, and Young Choi. This amazing group has played an essential role in creating accessible data experiences that meet people where they are.
