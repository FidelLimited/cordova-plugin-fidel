#import <Foundation/Foundation.h>

@interface FLSetupAdapter : NSObject

@property (nonatomic, readonly) NSDictionary *constantsToExport;

-(void)setupWith: (NSDictionary *)setupInfo;

@end
