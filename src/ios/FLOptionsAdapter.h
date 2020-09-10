#import <Foundation/Foundation.h>

@protocol FLCountryAdapter;
@protocol FLImageAdapter;
@protocol FLCardSchemesAdapter;

@interface FLOptionsAdapter : NSObject

@property (nonatomic, readonly) NSDictionary *constantsToExport;

-(instancetype)initWithimageAdapter:(id<FLImageAdapter>)imageAdapter
               cardSchemesAdapter:(id<FLCardSchemesAdapter>)cardSchemesAdapter;

-(void)setOptions: (NSDictionary *)options;

@end
