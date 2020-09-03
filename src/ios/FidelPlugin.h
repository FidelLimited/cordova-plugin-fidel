#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>

@interface FidelPlugin : CDVPlugin

- (void)setup:(CDVInvokedUrlCommand*)command;
- (void)setOptions:(CDVInvokedUrlCommand*)command;
- (void)openForm:(CDVInvokedUrlCommand*)command;

@end