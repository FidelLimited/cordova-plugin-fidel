
  Fidel.prototype = {
    openForm: function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Fidel", "openForm", []);
    },
  
    setup: function (successCallback, errorCallback, options) {
      var opts = options || {};
      cordova.exec(successCallback, errorCallback, "Fidel", "setup", [opts]);
    },

    setOptions: function (successCallback, errorCallback, options) {
        var opts = options || {};
        cordova.exec(successCallback, errorCallback, "Fidel", "setOptions", [opts]);
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
  