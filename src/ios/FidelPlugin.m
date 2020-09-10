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
#import "ImageAdapter.h"
#import "FLCardSchemesFromJSAdapter.h"
#import "FLRuntimeObjectToDictionaryAdapter.h"
#import "FLObjectToDictionaryAdapter.h"

@interface FidelPlugin()

@property (nonatomic, strong) FLOptionsAdapter *adapter;
@property (nonatomic, strong) FLSetupAdapter *setupAdapter;
@property (nonatomic, strong) id<FLObjectToDictionaryAdapter> objectAdapter;

@end

@implementation FidelPlugin


- (void)pluginInitialize {
    if (self) {

        id<FLImageAdapter> imageAdapter = [[ImageAdapter alloc] init];
        id<FLCardSchemesAdapter> cardSchemesAdapter = [[FLCardSchemesFromJSAdapter alloc] init];
        _adapter = [[FLOptionsAdapter alloc] initWithimageAdapter:imageAdapter
                                            cardSchemesAdapter:cardSchemesAdapter];
        _setupAdapter = [[FLSetupAdapter alloc] init];
        _objectAdapter = [[FLRuntimeObjectToDictionaryAdapter alloc] init];
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
