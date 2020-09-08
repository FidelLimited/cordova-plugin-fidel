
#import "FidelPlugin.h"
#import <Cordova/CDVPlugin.h>
#if __has_include(<Fidel/Fidel-Swift.h>)
#import <Fidel/Fidel-Swift.h>
#elif __has_include("Fidel-Swift.h")
#import "Fidel-Swift.h"
#else
#import "Fidel/Fidel-Swift.h" // Required when used as a Pod in a Swift project
#endif
#import "FLSetupAdapter.h"
#import "FLOptionsAdapter.h"
#import "FLCountryFromJSAdapter.h"
#import "ImageAdapter.h"
#import "FLCardSchemesFromJSAdapter.h"
#import "FLRNRuntimeObjectToDictionaryAdapter.h"

@interface FidelPlugin()

@property (nonatomic, strong) FLOptionsAdapter *adapter;
@property (nonatomic, strong) FLSetupAdapter *setupAdapter;
@property (nonatomic, strong) id<FLRNObjectToDictionaryAdapter> objectAdapter;

@end

@implementation FidelPlugin


- (void)pluginInitialize {
    if (self) {
        id<FLRNCountryAdapter> countryAdapter;
        countryAdapter = [[FLCountryFromJSAdapter alloc] init];
        id<FLRNImageAdapter> imageAdapter = [[ImageAdapter alloc] init];
        id<FLCardSchemesAdapter> cardSchemesAdapter = [[FLCardSchemesFromJSAdapter alloc] init];
        _adapter = [[FLOptionsAdapter alloc] initWithCountryAdapter:countryAdapter
                                                         imageAdapter:imageAdapter
                                                   cardSchemesAdapter:cardSchemesAdapter];
        _setupAdapter = [[FLSetupAdapter alloc] init];
        _objectAdapter = [[FLRNRuntimeObjectToDictionaryAdapter alloc] init];
    }
}

- (void)setup:(CDVInvokedUrlCommand *)command {
    NSDictionary* setupInfo = command.arguments[0];
    [self.setupAdapter setupWith:setupInfo];
}

 - (void)setOptions:(CDVInvokedUrlCommand*)command {
     NSDictionary* options = command.arguments[0];
     [self.adapter setOptions: options];
 }

- (void)openForm:(CDVInvokedUrlCommand*)command {
    UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
    __weak __typeof__(self) weakSelf = self;
    [FLFidel present:rootViewController onCardLinkedCallback:^(FLLinkResult * _Nonnull result) {
        NSDictionary *adaptedResult = [weakSelf.objectAdapter dictionaryFrom:result];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:adaptedResult];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } onCardLinkFailedCallback:^(FLLinkError * _Nonnull error) {
        NSDictionary *adaptedError = [weakSelf.objectAdapter dictionaryFrom:error];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:adaptedError];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

@end
