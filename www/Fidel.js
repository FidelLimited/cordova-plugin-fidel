
  Fidel.prototype = {
    openForm: function (successCallback, errorCallback, options) {
      var opts = options || {};
      cordova.exec(successCallback, errorCallback, "Fidel", "openForm", [opts]);
    },
  
    setup: function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Fidel", "setup", []);
    },

    setOptions: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Fidel", "setOptions", []);
      },
  };
  
  Fidel.install = function () {
    if (!window.plugins) {
      window.plugins = {};
    }
  
    window.plugins.fidel = new Fidel();
    return window.plugins.fidel;
  };
  
  cordova.addConstructor(Fidel.install);
  