
# Fidel Cordova Plugin

This plugin is a bridge between Cordova and Fidel's native iOS and Android SDKs. It helps you to add card linking technology to your Cordova apps in minutes. It captures credit/debit card numbers securely and links them to your programs.

![Demo GIF](https://cl.ly/a47b1852f029/Screen%252520Recording%2525202018-09-18%252520at%25252004.34%252520PM.gif)

## Getting started

`$ npm install cordova-fidel-plugin`

### iOS

**1.** Add iOS as a platform to your Cordova project:
`$ cordova platform add ios`

### Android

**1.** Add Android as a platform to your Cordova project:
`$ cordova platform add android`


**2.** Append Jitpack to `platforms/android/build.gradle`:

```java
allprojects {
  repositories {
    ...
    maven { url "https://jitpack.io" }
  }
}
```

**3.** Make sure that the `minSdkVersion` is the same or higher than the `minSdkVersion` of our native Android SDK:

```java
buildscript {
  ext {
    ...
    minSdkVersion = 19
    ...
  }
}
```

**4.** Our plugin contains native automated tests to ensure the quality of the plugin. To accomodate these tests, the plugin structure is a bit different than that of most plugins. Therefore, an extra step is needed after the plugin is installed: go to ```platforms/android/app/src/main/java/android/src/main/java``` and copy the ```com``` folder and its content to ```platforms/android/app/src/main/java/```, then delete the ```platforms/android/app/src/main/java/android/``` folder.

**5.** After you perform step #4, open ```platforms/android/app/src/main/java/com/fidelcordovalibrary/adapters/ImageFromReadableMapAdapter.java``` and change ```import com.fidelcordovalibrary.R;``` to your package name (e.g. ```import io.cordova.hellocordova.R;```)

## How to use Fidel's Cordova plugin

All Fidel functions are accesible via the ```window.plugins.fidelfunctions``` prefix.

There are 3 main steps to follow when setting up the plugin:

1. Set up Fidel with your API key and your program ID. Without them you can't open the Fidel card linking UI:

```javascript
window.plugins.fidelfunctions.setup(
  function(success){console.log(success)}, //success callback
  function(error){console.log(error)}, //error callback
    {
      apiKey:'your API key',
      programId: 'your program ID'
    }
);
```

2. Set the options that create the experience of linking a card with Fidel:

```javascript
//this is the default value for supported card schemes,
//but you can remove the support for some of the card schemes if you want to
const cardSchemes = new Set([
  FidelConstants.CardScheme.VISA,
  FidelConstants.CardScheme.MASTERCARD,
  FidelConstants.CardScheme.AMERICAN_EXPRESS
]);

window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)}, //success callback
  function(error) {console.log(error)}, //error callback
  {   
    showBannerImage: true,
    supportedCardSchemes: Array.from(cardSchemes),
    country: FidelConstants.Country.UNITED_STATES,
    autoScan: false,
    companyName: 'My Company', // the company name displayed in the checkbox text
    deleteInstructions: 'Your custom delete instructions!',
    privacyUrl: 'https://fidel.uk',
    termsConditionsUrl: 'https://fidel.uk/privacy', // mandatory when you set the default country to USA/Canada or when the user can select USA/Canada
    programName: 'My program name', // optional, is used when you set the default country to USA/Canada or when the user can select USA/Canada
    metaData: {'my-custom-key': 'my-custom-value'} // additional data to pass with the card
  }
);
```

3. Open the card linking view by calling:

```javascript
window.plugins.fidelfunctions.openForm(
  function(success) {console.log(success)}, // optional success callback
  function(error) {console.log(error)}, // optional error callback
);
```

Both `success` and `error` are objects that look like in the following examples:

### Success

```javascript
{
  accountId: "the-account-id"
  countryCode: "GBR" // the country selected by the user, in the Fidel SDK form
  created: "2019-04-22T05:26:45.611Z"
  expDate: "2023-12-31T23:59:59.999Z" // the card expiration date
  expMonth: 12 // for your convenience, this is the card expiration month
  expYear: 2023 // for your convenience, this is the card expiration year
  id: "card-id" // the card ID as registered on the Fidel platform
  lastNumbers: "4001" //last numbers of the card
  live: false
  mapped: false
  metaData: {meta-data-1: "value1"} //the meta data that you specified for the Fidel SDK
  programId: "your program ID, as specified for the Fidel SDK"
  scheme: "visa"
  type: "visa"
  updated: "2019-04-22T05:26:45.611Z"
}
```

### Error

```javascript
{
  code: "item-save" // the code of the error
  date: "2019-04-22T05:34:04.621Z" // the date of the card link request
  message: "Item already exists" // the message of the error
}
```

## Options documentation

### showBannerImage

Use this option to customize the topmost banner image with the Fidel UI. In order to do so, you must do the following:

**On Android**, overwrite the ```banner.png``` file from ```cordova-plugin-fidel/src/android/src/main/res/drawable-port-<density>```, where ```<density>``` is ```mdpi```, ```xhdpi``` and ```xxhdpi```. Note that you image has to be called ```banner.png``` in order to be displayed in the UI

**On iOS**, add an image called ```banner.png``` to your Xcode project. Make sure to add ```1x```, ```2x``` and ```3x``` image resources. Note that you image has to be called ```banner.png``` in order to be displayed in the UI

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    showBannerImage: true
  }
);
```

### country

Set a default country the SDK should use with:

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    country: FidelConstants.Country.UNITED_KINGDOM
  }
);
```

When you set a default country, the card linking screen will not show the country picker UI. The other options, for now, are: `.UNITED_STATES`, `.IRELAND`, `.SWEDEN`, `.JAPAN`, `.CANADA`.

### supportedCardSchemes

We currently support _Visa_, _Mastercard_ and _AmericanExpress_, but you can choose to support only one, two or all three. By default the SDK is configured to support all three.

If you set this option to an empty array or to `null`, of course, you will not be able to open the Fidel UI. You must support at least one our supported card schemes.

Please check the example below:

```javascript
const cardSchemes = new Set([
  FidelConstants.CardScheme.VISA,
  FidelConstants.CardScheme.MASTERCARD,
  FidelConstants.CardScheme.AMERICAN_EXPRESS
]);

window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    supportedCardSchemes: Array.from(cardSchemes)
  }
);
```

### autoScan

Set this property to `true`, if you want to open the card scanning UI immediately after executing `window.plugins.fidelfunctions.openForm`. The default value is `false`.

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    autoScan: true
  }
);
```

### metaData

Use this option to pass any other data with the card data:

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    metaData: {'my-custom-key': 'my-custom-value'}
  }
);
```

### companyName

Set your company name as it will appear in our consent checkbox text. Please set it to a maximum of 60 characters.

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    companyName: 'Your Company Name'
  }
);
```

### deleteInstructions

Write your custom opt-out instructions for your users. They will be displayed in the consent checkbox text as well.

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    deleteInstructions: 'Your custom card delete instructions!'
  }
);
```

### privacyUrl

This is the privacy policy URL that you can set for the consent checkbox text.

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    privacyUrl: 'https://fidel.uk',
  }
);
```

### programName (applied to the consent text only for USA and Canada)

Set your program name as it will appear in the consent text. Note that **this parameter is optional** and used when you set United States or Canada as the default country or don't set a default country (meaning that the user is free to select United States or Canada as their country). Please set it to a maximum of 60 characters.

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    programName: 'Your Program Name'
  }
);
```

### termsConditionsUrl (applied to the consent text only for USA and Canada)

This is the terms & conditions URL that you can set for the consent text. Note that **this parameter is mandatory** when you set United States or Canada as the default country or don't set a default country (meaning that the user is free to select United States or Canada as their country).

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    termsConditionsUrl: 'https://fidel.uk',
  }
);
```

## Customizing the consent text

In order to properly set the consent text, please follow these steps:

1. **Set the company name**

This parameter is optional, but we recommended setting it. If you don't set a company name, we'll show the default value in the consent text: ```Your Company Name```

2. **Set the privacy policy URL**

This is an optional parameter. It is added as a hyperlink to the ```privacy policy``` text. Please see the full behaviour below.

3. **Set the delete instructions**

Optional parameter whose default value is ```going to your account settings```. This default value is applied for both consent texts - for the USA & Canada as well as for the rest of the world.

4. **Set the card scheme name**

By default, we allow the user to input card numbers from either Visa, Mastercard or American Express, but you can control which card networks you accept. The consent text changes based on what you define or based on what the user inputs. Please see the full behaviour below.

5. **Set the program name (applied to the consent text only for USA and Canada)**

This parameter is taken into account only for USA and Canada. The default value for program name is ```our```. 

6. **Set the terms and conditions URL (applied to the consent text only for USA and Canada)**

This parameter is mandatory for USA and Canada. Once set, it will be applied as a hyperlink on the ```Terms and Conditions``` text.


Note that the consent text has a different form depending on the country you set or the country the user can select. Below you can find the specifics for each case.

### Consent text for United States and Canada

When you set United States or Canada as the default country or don't set a default country (meaning that the user is free to select United States or Canada as their country), a specific consent text will be applied.

For USA & Canada, the following would be an example Terms & Conditions text for ```Cashback Inc``` (an example company) that uses ```Awesome Bonus``` as their program name:

*By submitting your card information and checking this box, you authorize ```card_scheme_name``` to monitor and share transaction data with Fidel (our service provider) to participate in ```Awesome Bonus``` program. You also acknowledge and agree that Fidel may share certain details of your qualifying transactions with ```Cashback Inc``` to enable your participation in ```Awesome Bonus``` program and for other purposes in accordance with the ```Cashback Inc``` Terms and Conditions, ```Cashback Inc``` privacy policy and Fidel’s Privacy Policy. You may opt-out of transaction monitoring on the linked card at any time by ```deleteInstructions```.*

There are two specific parameters that you can set for this consent text:

#### 1. termsConditionsUrl
This parameter is mandatory when you set United States or Canada as the default country or don't set a default country. When you set this parameter, the ```Terms and Conditions``` from the consent text will get a hyperlink with the URL you set.

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    termsConditionsUrl: 'https://fidel.uk',
  }
);
```

If you don't set this parameter, you'll get an error when trying to open the card linking interface: ```You have set a North American default country or you allow the user to select a North American country. For North American countries it is mandatory for you to provide the Terms and Conditions URL.```

#### 2. programName
This parameter is optional when you set United States or Canada as the default country or don't set a default country. If you don't set a program name, we'll use ```our``` as the default value (for example, in the text above, you would see *...to monitor and share transaction data with Fidel (our service provider) to participate in ```our``` program...*)

```javascript
window.plugins.fidelfunctions.setOptions(
  function(success) {console.log(success)},
  function(error) {console.log(error)},
  {   
    programName: 'Your Program Name'
  }
);
```

#### Consent text behaviour for card scheme name

If you don't set a card scheme (meaning the user can input either Visa, Mastercard or American Express cards) *OR* set 2 or 3 card scheme names, the default value used will be ```your payment card network``` (e.g. _you authorize ```your payment card network``` to monitor and share transaction data with Fidel (our service provider)_). When the user starts typing in a card number, ```your payment card network``` will be replaced with the scheme name of the card that they typed in (e.g. Visa).

If you set one card scheme name, it will be displayed in the consent text (e.g. for Mastercard it would be _you authorize ```Mastercard``` to monitor and share transaction data with Fidel (our service provider)_) This value - ```Mastercard``` - will not change when the user starts typing in a card number.

#### Consent text behaviour for privacy policy

Notice the following excerpt from the consent text above: _in accordance with the ```Cashback Inc``` Terms and Conditions, ```Cashback Inc``` privacy policy and Fidel’s Privacy Policy_. If you set a ```privacyUrl```, this is the text that will be displayed, along with a hyperlink set on _privacy policy_.
If you do not set a ```privacyUrl```, the text will become _in accordance with the ```Cashback Inc``` Terms and Conditions and Fidel’s Privacy Policy._

### Consent text for the rest of the world

When you set United Kingdom, Ireland, Japan or Sweden as the default country or the user selects one of these countries from the list, a consent text specific for these countries will be applied.

The following would be an example Terms & Conditions text for ```Cashback Inc``` (an example company):

*I authorise ```card_scheme_name``` to monitor my payment card to identify transactions that qualify for a reward and for ```card_scheme_name``` to share such information with ```Cashback Inc```, to enable my card linked offers and target offers that may be of interest to me. For information about ```Cashback Inc``` privacy practices, please see the privacy policy. You may opt-out of transaction monitoring on the payment card you entered at any time by ```deleteInstructions```.*

#### Consent text behaviour for card scheme name

If you don't set a card scheme (meaning the user can input either Visa, Mastercard or American Express cards) *OR* set 2 or 3 card scheme names, the default value used will be ```my card network``` (e.g. _I authorise ```my card network``` to monitor my payment card_). When the user starts typing in a card number, ```my card network``` will be replaced with the scheme name of the card that they typed in (e.g. Visa).

If you set one card scheme name, it will be displayed in the consent text (e.g. for Mastercard it would be _I authorise ```Mastercard``` to monitor my payment card_) This value - ```Mastercard``` - will not change when the user starts typing in a card number.

#### Consent text behaviour for privacy policy

If you do not set a privacy policy URL, the privacy policy related phrase will be removed from the text.

Notice the following excerpt from the consent text above: _...may be of interest to me. For information about ```Cashback Inc``` privacy practices, please see the privacy policy. You may opt-out of..._ If you set a ```privacyUrl```, this is the text that will be displayed, along with a hyperlink set on *privacy policy*.

If you do not set a ```privacyUrl```, the text will become _...may be of interest to me. You may opt-out of..._

## Localisation

The SDK's default language is English, but it's also localised for French and Swedish languages. When the device has either `Français (Canada)` or `Svenska (Sverige)` as its language, the appropriate texts will be displayed. Please note that developer error messages are in English only and they will not be displayed to the user.

Please make sure that your project also supports localisation for the languages that you want to support.

## Test card numbers

In the test environment please use our VISA, Mastercard or American Express test card numbers. You must use a test API Key for them to work.

VISA: _4444000000004***_ (the last 3 numbers can be anything)

Mastercard: _5555000000005***_ (the last 3 numbers can be anything)

American Express: _3400000000003**_ or _3700000000003**_ (the last 2 numbers can be anything)

## Feedback

The Fidel SDK is in active development, we welcome your feedback!

Get in touch:

GitHub Issues - For SDK issues and feedback
Fidel Developers Forum - [https://community.fidel.uk](https://community.fidel.uk) - for personal support at any phase of integration
