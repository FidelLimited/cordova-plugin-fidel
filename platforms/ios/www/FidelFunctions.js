  function FidelFunctions() {
  }

  FidelFunctions.prototype = {
    openForm: function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "FidelPlugin", "openForm", []);
    },
  
    setup: function (successCallback, errorCallback, options) {
      var opts = options || {};
      cordova.exec(successCallback, errorCallback, "FidelPlugin", "setup", [opts]);
    },

    setOptions: function (successCallback, errorCallback, options) {
        var opts = options || {};
        cordova.exec(successCallback, errorCallback, "FidelPlugin", "setOptions", [opts]);
    },
  };
  
  FidelFunctions.install = function () {
    if (!window.plugins) {
      window.plugins = {};
    }
  
    window.plugins.fidelfunctions = new FidelFunctions();
    return window.plugins.fidelfunctions;
  };
  
  cordova.addConstructor(FidelFunctions.install);
  